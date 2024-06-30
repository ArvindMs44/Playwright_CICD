package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

public class TestLetCode {
    static Playwright playwright;
    static Browser browser;
    static BrowserContext context;
    static Page page;

    @BeforeAll
    public static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    public static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    public void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        page = context.newPage();
    }

    @AfterEach
    public void closeContext() {
        context.close();
    }

    @Test
    public void TestInput(){
        page.navigate("https://letcode.in/test");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Edit")).click();
        page.getByPlaceholder("Enter first & last name").fill("Aravind");
        page.locator("#clearMe").fill("Koushik");
        assertThat(page.locator("#noEdit")).isDisabled();
    }

    @Test
    public void TestDropdown(){
        page.navigate("https://letcode.in/dropdowns");
        page.locator("#fruits").selectOption("1");
        assertThat(page.locator(".subtitle")).containsText("You have selected Mango");
    }

    @Test
    public void TestAlerts(){
        page.navigate("https://letcode.in/alert");
        page.locator(("#confirm")).click();
        page.onDialog(dialog -> dialog.accept());
        page.locator(("#confirm")).click();
        page.onDialog(dialog -> dialog.dismiss());
    }

    @Test
    public void TestDragDrop(){
        page.navigate("https://letcode.in/dropable");
        Locator draggable = page.locator(("#draggable"));
        Locator droppable = page.locator("#droppable");
        draggable.dragTo(droppable);
        assertThat(page.locator("div[id='droppable'] p")).containsText("Dropped!");
    }

    @Test
    public void TestFrames(){
        page.navigate("https://letcode.in/frame");
        FrameLocator frame = page.frameLocator("#firstFr");
        Locator firstname = frame.getByPlaceholder("Enter name");
        Locator lastname = frame.getByPlaceholder("Enter email");
        firstname.fill("John");
        lastname.fill("Snow");
        assertThat(frame.locator(".title.has-text-info")).containsText("You have entered John Snow");
    }

    @Test
    public void TestUpload(){
        page.navigate("https://practice.expandtesting.com/upload");
        page.locator("#fileInput").setInputFiles(Paths.get("C:/Users/arvin/OneDrive/Documents/Test.txt/"));
        page.locator("#fileSubmit").click();
        assertThat(page.locator("div[id='uploaded-files'] p")).containsText("_Test.txt");
    }

    @Test
    public void TestDownload(){
        page.navigate("https://letcode.in/file");
        Download download = page.waitForDownload(() -> {page.locator("#pdf").click();});
        download.saveAs(Paths.get("C:/Users/arvin/OneDrive/Documents/", download.suggestedFilename()));
    }
}