package goodreads;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoodreadsUITest {
	private WebDriver driver;

	@BeforeClass
	public void setUp() {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = WebDriverManager.chromedriver().capabilities(options).create();
		driver.manage().window().maximize();

	}

	@Test
	public void validateBookFunctions() throws InterruptedException {

		// 1. Login to goodreads.com
		driver.get("https://www.goodreads.com/");
		WebElement signInLink = driver.findElement(By.xpath("//a[text()='Sign In']"));
		signInLink.click();

		WebElement signInWithEmailButton = driver
				.findElement(By.xpath("//button[contains(text(),'Sign in with email')]"));
		signInWithEmailButton.click();

		WebElement emailField = driver.findElement(By.id("ap_email"));
		emailField.sendKeys("chethupoojary300@gmail.com");
		WebElement passwordField = driver.findElement(By.id("ap_password"));
		passwordField.sendKeys("goodreads");
		WebElement signInButton = driver.findElement(By.xpath("//input[@id='signInSubmit']"));
		signInButton.click();

		// 2. Search for a specific book title.

		String bookName = "The Great Gatsby";
		WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search books']"));
		searchBox.sendKeys(bookName);
		WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
		searchButton.click();

		// 3. Mark it as ‘want to read’
		WebElement wantToReadButton = driver.findElement(
				By.xpath("(//span[text()='" + bookName + "'])[1]/../..//div//div//button[@class='wtrToRead']"));
		wantToReadButton.click();
		Thread.sleep(5000);

		// 4. Remove the selected book from my book list.

		driver.findElement(By.xpath(
				"//nav[@class='siteHeader__primaryNavInline']//a[@class='siteHeader__topLevelLink'][normalize-space()='My Books']"))
				.click();
		driver.findElement(By.xpath("//img[@title='Remove from my books']")).click();
		driver.switchTo().alert().accept();
		WebElement bookCheckbox1 = driver
				.findElement(By.xpath("//div[contains(text(),'The Great Gatsby was removed from your books.')]"));
		String expectedMsg = bookCheckbox1.getText();
		Assert.assertEquals(expectedMsg, bookName + " was removed from your books.");

		// 5. Logout
		WebElement userDropdown = driver.findElement(By.xpath("(//img[@alt='Chethan'])[1]"));
		userDropdown.click();
		WebElement signOutButton = driver.findElement(By.xpath("(//a[text()='Sign out'])[1]"));
		signOutButton.click();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}