package br.ce.wcaquino.estrategia4;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Test;
import br.ce.wcaquino.dao.SaldoDAO;
import br.ce.wcaquino.dao.impl.SaldoDAOImpl;
import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.dbunit.ImportExport;
import br.ce.wcaquino.utils.DataUtils;

public class CalculoSaldoTest {
	//um usuario
	//uma conta
	//uma transacao
	
	//deve considerar transacoes do mesmo usuario
	//deve considerar transacoes da mesma conta
	//deve considerar transacoes pagas
	//deve considerar transacoes ate a data corrente
	//deve somar receitas
	//deve subtrair despesas
	
	@Test
	public void deveCalcularSaldoCorreto() throws Exception {
	/*	UsuarioService usuarioService = new UsuarioService();
		ContaService contaService = new ContaService();
		TransacaoService transacaoService = new TransacaoService();
		
		//usuarios
		Usuario usuario = usuarioService.salvar(new Usuario("Usuário", "email@email.com", "123"));
		Usuario usuarioAlternativo = usuarioService.salvar(new Usuario("Usuário Alternativo", "email123@qualqueremail.com", "123"));
		
		//contas
		Conta conta = contaService.salvar(new Conta("Conta Principal", usuario.getId()));
		Conta contaSecundaria = contaService.salvar(new Conta("Conta Secundaria", usuario.getId()));
		Conta contaUsuarioAlternativo = contaService.salvar(new Conta("Conta Usuario Alternativo", usuarioAlternativo.getId()));
		
		//transacoes
		//transacao correta / Saldo = 2
		transacaoService.salvar(new Transacao("Transacao Inicial", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 2d, true, conta, usuario));
		
		//Transacao usuario Alternativo / Saldo = 2 
		transacaoService.salvar(new Transacao("Transacao outro Usuario", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 4d, true, contaUsuarioAlternativo, usuarioAlternativo));
		
		//Transacao de outra conta / Saldo = 2
		transacaoService.salvar(new Transacao("Transacao outro Conta", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 8d, true, contaSecundaria, usuario));
		
		//Transacao pendente / Saldo = 2
		transacaoService.salvar(new Transacao("Transacao pendente", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 16d, false, conta, usuario));
		
		//Transacao passada / Saldo = 34
		transacaoService.salvar(new Transacao("Transacao passada", "Envolvido", TipoTransacao.RECEITA,
				obterDataComDiferencaDias(-1), 32d, true, conta, usuario));
		
		//Transacao passada / Saldo = 34
		transacaoService.salvar(new Transacao("Transacao futura", "Envolvido", TipoTransacao.RECEITA,
				obterDataComDiferencaDias(1), 64d, true, conta, usuario));
		
		//Transacao despesa / Salda = -94
		transacaoService.salvar(new Transacao("Transacao despesa", "Envolvido", TipoTransacao.DESPESA,
				new Date(), 128d, true, conta, usuario));
		
		//Testes de saldo com valor negativo dá azar / Saldo = 162
		transacaoService.salvar(new Transacao("Transacao da sorte", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 256d, true, conta, usuario));
		*/
		//ImportExport.importarBanco("saldo.xml");
		
		DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet dataSet = builder.build(new FileInputStream("massas" + File.separator + "saldo.xml"));
		
		ReplacementDataSet dataSetAlterado = new ReplacementDataSet(dataSet);
		dataSetAlterado.addReplacementObject("[hoje]", new Date());
		dataSetAlterado.addReplacementObject("[ontem]", DataUtils.obterDataComDiferencaDias(-1));
		dataSetAlterado.addReplacementObject("[amanha]", DataUtils.obterDataComDiferencaDias(1));
		
		DatabaseOperation.CLEAN_INSERT.execute(dbConn, dataSetAlterado);
		
		SaldoDAO saldoDAO = new SaldoDAOImpl();
		Assert.assertEquals(new Double(162d), saldoDAO.getSaldoConta(1L));
		Assert.assertEquals(new Double(8d), saldoDAO.getSaldoConta(2L));
		Assert.assertEquals(new Double(4d), saldoDAO.getSaldoConta(3L));
	}
	
/*	public Date obterDataComDiferencaDias(int dias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}*/
}
