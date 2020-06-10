package br.ce.wcaquino.estrategia4;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.dbunit.ImportExport;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;

public class ContaServiceTestDbUnit {
	
	//static Faker faker = new Faker();
	
	ContaService service = new ContaService();
	UsuarioService userService = new UsuarioService();
	//private static Usuario usuarioGlobal;
	//private Conta contaTeste;
	
/*	@Before
	public void inserirConta() throws Exception {
		Connection conn = ConnectionFactory.getConnection();
		conn.createStatement().executeUpdate("DELETE  FROM transacoes");
		conn.createStatement().executeUpdate("DELETE  FROM contas");
		conn.createStatement().executeUpdate("DELETE  FROM usuarios");
		conn.createStatement().executeUpdate("INSERT INTO Usuarios (id, nome, email, senha) "
				+ "VALUES (1, 'Usuario de controle', 'ricardocaputo@gmail.com', '1234567890')");
	}*/
	
	@Test
	public void testInserir() throws Exception {
		ImportExport.importarBanco("est4_inserirConta.xml");
		Usuario usuario = userService.findById(1L);
		Conta conta = new Conta("Conta Salva", usuario);
		Conta contaSalva = service.salvar(conta);
		Assert.assertNotNull(contaSalva.getId());
		userService.printAll();
		service.printAll();
	}
	
	@Test
	public void testInserir_Assertion() throws Exception {
		ImportExport.importarBanco("est4_inserirConta.xml");
		Usuario usuario = userService.findById(1L);
		Conta conta = new Conta("Conta Salva", usuario);
		Conta contaSalva = service.salvar(conta);
		
		DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		IDataSet estadoFinalBanco = dbConn.createDataSet();
		
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet dataSetEsperado = builder.build(new FileInputStream("massas" + File.separator + "est4_inserirConta_saida.xml"));
		
		//Assertion.assertEquals(dataSetEsperado, estadoFinalBanco);
		
		DiffCollectingFailureHandler handler = new DiffCollectingFailureHandler();
		Assertion.assertEquals(dataSetEsperado, estadoFinalBanco, handler);
		
		boolean erroReal = false;
		List<Difference> erros = handler.getDiffList();
		for(Difference erro: erros) {
			System.out.println(erro.toString());
			if(erro.getActualTable().getTableMetaData().getTableName().equals("contas")) {
				if(erro.getColumnName().equals("id")) {
					if(erro.getActualValue().toString().equals(contaSalva.getId().toString())) {
						continue;
					} else {
						System.out.println("Id errado mesmo!");
						erroReal = true;
					}
				} else {
					erroReal = true;
				}
			} else {
				erroReal = true;
			}
		}
		Assert.assertFalse(erroReal);
		
	}
	
	@Test
	public void testInserir_Filter() throws Exception {
		ImportExport.importarBanco("est4_inserirConta.xml");
		Usuario usuario = userService.findById(1L);
		Conta conta = new Conta("Conta Salva", usuario);
		service.salvar(conta);
		
		DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		IDataSet estadoFinalBanco = dbConn.createDataSet();
		
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet dataSetEsperado = builder.build(new FileInputStream("massas" + File.separator + "est4_inserirConta_saida.xml"));
		
		//Assertion.assertEquals(dataSetEsperado, estadoFinalBanco);
		
		ITable contasAtualFiltradas = DefaultColumnFilter.excludedColumnsTable(estadoFinalBanco.getTable("contas"), new String[] {"id"});
		
		ITable contasEsperadoFiltradas = DefaultColumnFilter.excludedColumnsTable(dataSetEsperado.getTable("contas"), new String[] {"id"});
		
		ITable usuarioAtualFiltradas = DefaultColumnFilter.excludedColumnsTable(estadoFinalBanco.getTable("usuarios"), new String[] {"conta_principal_id"});
		
		ITable usuarioEsperadoFiltradas = DefaultColumnFilter.excludedColumnsTable(dataSetEsperado.getTable("usuarios"), new String[] {"conta_principal_id"});
		
		
		Assertion.assertEquals(contasEsperadoFiltradas, contasAtualFiltradas);
		Assertion.assertEquals(usuarioEsperadoFiltradas, usuarioAtualFiltradas);
	}	
	
	@Test
	public void testAlterar() throws Exception {
		/*ConnectionFactory.getConnection().createStatement().executeUpdate(
				"INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");*/
		
		ImportExport.importarBanco("est4_umaConta.xml");
		Conta contaTeste = service.findByName("Conta para Testes");
		service.printAll();
		contaTeste.setNome("Conta para Testes - Alterada!");
		Conta contaAlterada = service.salvar(contaTeste);
		
		Assert.assertEquals("Conta para Testes - Alterada!", contaAlterada.getNome());
		service.printAll();
	}
	
	@Test
	public void testConsultar() throws Exception {
		/*ConnectionFactory.getConnection().createStatement().executeUpdate(
				"INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");*/
		ImportExport.importarBanco("est4_umaConta.xml");
		Conta contaBuscada = service.findById(1L);
		
		Assert.assertEquals("Conta para Testes", contaBuscada.getNome());
	}
	
	@Test
	public void testExcluir() throws Exception {
		/*ConnectionFactory.getConnection().createStatement().executeUpdate(
				"INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");*/
		
		ImportExport.importarBanco("est4_umaConta.xml");
		Conta contaTeste = service.findByName("Conta para Testes");
		service.printAll();
		service.delete(contaTeste);
		
		Conta contaBuscada = service.findById(contaTeste.getId());
		Assert.assertNull(contaBuscada);
		service.printAll();
	}
}
