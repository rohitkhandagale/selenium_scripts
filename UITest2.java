package Automation.TestAutomation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UITest2 {

	private WebDriver driver;
	private SoftAssert softAssert = new SoftAssert();

	@BeforeMethod
	public void setupWebDriver() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		System.setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	@AfterMethod
	public void tearDownWebDriver() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testBrokenLinks() throws IOException {
		long startTime = System.currentTimeMillis();

		driver.get("https://rahulshettyacademy.com/AutomationPractice/");

		List<WebElement> links = driver.findElements(By.xpath("//*[@class=\"gf-t\"]//ul/li/a"));
		for (WebElement link : links) {
			try {
				URL url = new URL(link.getAttribute("href"));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("HEAD");
				conn.connect();
				int responseCode = conn.getResponseCode();
				if (responseCode >= 400) {
					System.out.println(link.getAttribute("href"));
					softAssert.fail("Link is broken: " + link.getAttribute("href"));
				}
			} catch (IOException e) {
				e.printStackTrace();
				softAssert.fail("Exception occurred: " + e.getMessage());
			}
		}

		long endTime = System.currentTimeMillis();
		long durationSequential = endTime - startTime;
		System.out.println("Sequential execution time: " + durationSequential + " milliseconds");

		softAssert.assertAll();
	}

	@Test
	public void testBrokenLinksParallel() throws IOException {
		long startTime = System.currentTimeMillis();

		driver.get("https://rahulshettyacademy.com/AutomationPractice/");

		List<WebElement> links = driver.findElements(By.xpath("//*[@class=\"gf-t\"]//ul/li/a"));
		links.parallelStream().forEach(link -> {
			try {
				URL url = new URL(link.getAttribute("href"));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("HEAD");
				conn.connect();
				int responseCode = conn.getResponseCode();
				if (responseCode >= 400) {
					System.out.println(link.getAttribute("href"));
					softAssert.fail("Link is broken: " + link.getAttribute("href"));
				}
			} catch (IOException e) {
				e.printStackTrace();
				softAssert.fail("Exception occurred: " + e.getMessage());
			}
		});

		long endTime = System.currentTimeMillis();
		long durationParallel = endTime - startTime;
		System.out.println("Parallel execution time: " + durationParallel + " milliseconds");

		softAssert.assertAll();
	}

}
