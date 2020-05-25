package br.ce.wcaquino.estrategia1;

import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContaTesteWeb {

	private static FirefoxDriver driver;

	@BeforeClass
	public static void login() {
		System.setProperty("webdriver.firefox.driver",
				"C:\\Users\\ricardo.couto\\Documents\\automacaoWK\\driver\\geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		driver = new FirefoxDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://seubarriga.wcaquino.me/");
		driver.findElement(By.id("email")).sendKeys("ricardocaputo@gmail.com");
		driver.findElement(By.id("senha")).sendKeys("1234567890");
		driver.findElement(By.tagName("button")).click();
	}

	@Test
	public void test1_inserir() {
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Adicionar")).click();
		driver.findElement(By.id("nome")).sendKeys("Conta Estrategia #1");
		driver.findElement(By.tagName("button")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta adicionada com sucesso!", msg);
	}
	
	@Test
	public void test2_consultar() {
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'Conta Estrategia #1')]/..//a")).click();
		String nomeConta = driver.findElement(By.id("nome")).getAttribute("value");
		Assert.assertEquals("Conta Estrategia #1", nomeConta);
	}
	
	@Test
	public void test3_alterar() {
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'Conta Estrategia #1')]/..//a")).click();
		driver.findElement(By.id("nome")).clear();
		driver.findElement(By.id("nome")).sendKeys("Conta Estrategia #1 - Alterada");
		driver.findElement(By.tagName("button")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta alterada com sucesso!", msg);
	}
	
	@Test
	public void test4_excluir() {
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Listar")).click();
		driver.findElement(By.xpath("//td[contains(text(),'Conta Estrategia #1 - Alterada')]/..//a[2]")).click();
		String msg = driver.findElement(By.xpath("//div[@class='alert alert-success']")).getText();
		Assert.assertEquals("Conta removida com sucesso!", msg);
	}

	@AfterClass
	public static void fechar() {
		driver.quit();
	}
}
