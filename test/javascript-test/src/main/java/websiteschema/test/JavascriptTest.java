package websiteschema.test;

import com.thoughtworks.selenium.*;

public class JavascriptTest {

    public static void main(String args[]) {
        String seleniumServer = args[0];// localhost
        int seleniumPort = Integer.parseInt(args[1]);// 4444
        String browser = args[2];// *firefox
        String rootUrl = args[3];// http://localhost/
        String path = args[4];// /~twer/max-prob-segment/corpus-editor/test/unit/runner.html
        Selenium selenium = new DefaultSelenium(seleniumServer, seleniumPort, browser, rootUrl);
        selenium.start();
        boolean failed = false;
        try {
            selenium.open(path);
            selenium.waitForPageToLoad("30000");
            System.out.println(selenium.getXpathCount("//li[@class='passed']") + " specs passed.");
            Number failedCount = selenium.getXpathCount("//li[@class='failed']");
            failed = failedCount.intValue() > 0;
            System.out.println(failedCount + " specs failed.");
            System.out.println(selenium.getBodyText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            selenium.stop();
        }
        if (failed) throw new NullPointerException("failed");
    }
}
