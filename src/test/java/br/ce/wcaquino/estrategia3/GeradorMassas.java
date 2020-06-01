package br.ce.wcaquino.estrategia3;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.github.javafaker.Faker;

import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;

public class GeradorMassas {
	
	private static FirefoxDriver driver;
	private Faker faker = new Faker();
	
	public static final String CHAVE_CONTA_SB = "CONTA_SB";
	public static final String CHAVE_CONTA = "CONTA";
	
	public void gerarContaSeuBarriga() throws ClassNotFoundException, SQLException {
		
		System.setProperty("webdriver.firefox.driver",
				"C:\\Users\\ricardo.couto\\Documents\\automacaoWK\\driver\\geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);//false
		driver = new FirefoxDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://seubarriga.wcaquino.me/");
		driver.findElement(By.id("email")).sendKeys("ricardocaputo@gmail.com");
		driver.findElement(By.id("senha")).sendKeys("1234567890");
		driver.findElement(By.tagName("button")).click();
		
		String registro = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();
		driver.findElement(By.linkText("Contas")).click();
		driver.findElement(By.linkText("Adicionar")).click();
		driver.findElement(By.id("nome")).sendKeys(registro);
		driver.findElement(By.tagName("button")).click();
		
		driver.quit();
		
		new MassaDAOImpl().inserirMassa(CHAVE_CONTA_SB, registro);
		
	}
	
	public void gerarConta() throws Exception {
		Faker faker = new Faker();
		ContaService service = new ContaService();
		UsuarioService userService = new UsuarioService();
		//private static Usuario usuarioGlobal;
		//private Conta contaTeste;
		
		
		Usuario usuarioGlobal = new Usuario(faker.name().fullName(), faker.internet().emailAddress(), faker.internet().password());
		Usuario usuarioSalvo = userService.salvar(usuarioGlobal);
		Conta conta = new Conta(faker.superhero().name(), usuarioSalvo);
		service.salvar(conta);
		
		new MassaDAOImpl().inserirMassa(CHAVE_CONTA, conta.getNome());
		
		
	}
	
	public static void main(String[] args) throws Exception {
		/*GeradorMassas gerador = new GeradorMassas();
		for(int i = 0 ; i < 10; i++) {
			System.out.println("Gerado a massa # : " + i);
			//gerador.gerarContaSeuBarriga(); >> funcional
			gerador.gerarConta();
		}*/
		
		String obterMassa = new MassaDAOImpl().obterMassa(CHAVE_CONTA_SB);
		System.out.println(obterMassa);
	}

}
