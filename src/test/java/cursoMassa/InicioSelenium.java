package cursoMassa;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

public class InicioSelenium {

	public static void main(String[] args) {
		System.setProperty("webdriver.firefox.driver", "C:\\Users\\ricardo.couto\\Documents\\automacaoWK\\driver\\geckodriver.exe");
		
		FirefoxDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://seubarriga.wcaquino.me/");
		driver.findElement(By.id("email")).sendKeys("ricardocaputo@gmail.com");
		driver.findElement(By.id("senha")).sendKeys("1234567890");
		driver.findElement(By.tagName("button")).click();
		
		driver.quit();
	}	
}
