package br.ce.wcaquino.estrategia5;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
	
	@BeforeClass
	public static void inserirConta() throws Exception {
		ImportExport.importarBanco("est5.xml");
		
		/*Connection conn = ConnectionFactory.getConnection();
		conn.createStatement().executeUpdate("DELETE  FROM transacoes");
		conn.createStatement().executeUpdate("DELETE  FROM contas");
		conn.createStatement().executeUpdate("DELETE  FROM usuarios");
		
		conn.createStatement().executeUpdate("INSERT INTO Usuarios (id, nome, email, senha) "
				+ "VALUES (1, 'Usuario de controle', 'ricardocaputo@gmail.com', '1234567890')");
		conn.createStatement().executeUpdate("INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");
		conn.createStatement().executeUpdate("INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (2, 'Conta CT005', 1)");
		conn.createStatement().executeUpdate("INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (3, 'Conta para deletar', 1)");*/
	}
	
	@Test
	public void testInserir() throws Exception {
		Usuario usuario = userService.findById(1L);
		Conta conta = new Conta("Conta Salva", usuario);
		Conta contaSalva = service.salvar(conta);
		Assert.assertNotNull(contaSalva.getId());
		userService.printAll();
		service.printAll();
	}
	
	@Test
	public void testAlterar() throws Exception {
		/*ConnectionFactory.getConnection().createStatement().executeUpdate(
				"INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");*/

		Conta contaTeste = service.findByName("Conta CT005");
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
		Conta contaBuscada = service.findById(1L);
		
		Assert.assertEquals("Conta para Testes", contaBuscada.getNome());
	}
	
	@Test
	public void testExcluir() throws Exception {
		/*ConnectionFactory.getConnection().createStatement().executeUpdate(
				"INSERT INTO contas (id, nome, usuario_id) "
				+ "VALUES (1, 'Conta para Testes', 1)");*/

		Conta contaTeste = service.findByName("Conta para deletar");
		service.printAll();
		service.delete(contaTeste);
		
		Conta contaBuscada = service.findById(contaTeste.getId());
		Assert.assertNull(contaBuscada);
		service.printAll();
	}
}
