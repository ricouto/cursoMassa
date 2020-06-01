package br.ce.wcaquino.estrategia3;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.github.javafaker.Faker;

public class ContaTesteWeb {

	private static FirefoxDriver driver;
	private Faker faker = new Faker();

	@BeforeClass
	public static void login() {
		System.setProperty("webdriver.firefox.driver",
				"C:\\Users\\ricardo.couto\\Documents\\automacaoWK\\driver\\geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);//true
		driver = new FirefoxDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://seubarriga.wcaquino.me/");
		driver.findElement(By.id("email")).sendKeys("ricardocaputo@gmail.com");
		driver.findElement(By.id("senha")).sendKeys("1234567890");
		driver.findElement(By.tagName("button")).click();
	}

	@Test
	public void inserir() throws ClassNotFoundException, SQLException {
		String conta = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();
		
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Adicionar")).click();
		driver.findElement(By.id("nome")).sendKeys(conta);
		driver.findElement(By.tagName("button")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta adicionada com sucesso!", msg);
		new MassaDAOImpl().inserirMassa(GeradorMassas.CHAVE_CONTA_SB, conta);
	}
	
	@Test
	public void consultar() throws ClassNotFoundException, SQLException {
		//String conta = inserirConta();
		String conta = new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA_SB);
		
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'" +conta+ "')]/..//a")).click();
		String nomeConta = driver.findElement(By.id("nome")).getAttribute("value");
		Assert.assertEquals(conta, nomeConta);
		new MassaDAOImpl().inserirMassa(GeradorMassas.CHAVE_CONTA_SB, conta);
	}
	
	@Test
	public void alterar() throws ClassNotFoundException, SQLException {
		//String conta = inserirConta();
		String conta = new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA_SB);
		
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'" +conta+ "')]/..//a")).click();
		driver.findElement(By.id("nome")).sendKeys(" - Alterada");
		driver.findElement(By.tagName("button")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta alterada com sucesso!", msg);
	}
	
	@Test
	public void excluir() throws ClassNotFoundException, SQLException {
		//String conta = inserirConta();
		String conta = new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA_SB);
		
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'" +conta+ "')]/..//a[2]")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta removida com sucesso!", msg);
	}
	
/*	public String inserirConta() {
		String registro = faker.harryPotter().character();
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Adicionar")).click();
		driver.findElement(By.id("nome")).sendKeys(registro);
		driver.findElement(By.tagName("button")).click();
		return registro;
	}*/

	@AfterClass
	public static void fechar() {
		driver.quit();
	}
}
