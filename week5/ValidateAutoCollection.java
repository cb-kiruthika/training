/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chargebee.qa.app.test;

import com.chargebee.app.configweb.Routes;
import com.chargebee.qa.app.TestRunner;
import com.chargebee.qa.core.SeleniumBase;
import com.chargebee.qa.data.DevData;
import com.chargebee.qa.util.ChosenUtils;
import com.chargebee.qa.util.ClickElUtil;
import com.chargebee.qa.util.GetElUtil;
import com.chargebee.qa.util.LoginHelper;
import com.chargebee.qa.util.Select2Util;
import com.chargebee.qa.util.SetElUtil;
import com.chargebee.qa.util.UrlHelper;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author cb-kiruthika
 */
public class ValidateAutoCollection extends SeleniumBase{
    public static void main(String[] args) throws Throwable{
        new TestRunner().testThisClass().run();
    }
    
    @BeforeClass
    void signin() throws Exception{
        new DevData().signup(true).load();
        LoginHelper.loginMannarTest();
    }
    
    @Test(description = "Selenium test")
    void testMethod(){
        // UrlHelper.loadURL(Routes.rsubscriptions.neww);
        // SetElUtil.setText(By.id("customer.handle"), "aaa");
        // List<WebElement> planOptions = Select2Util.getFilterOptionList("plan_code");
        // Select2Util.selectTextByName("plan_code", planOptions.get(1).getText(), true);
        // ClickElUtil.click(By.id("customer.auto_collection_200"), true);
        // ClickElUtil.clickSubmitBtn();
        // ClickElUtil.click("cb-popup-confirm-submit");
        UrlHelper.loadURL(Routes.rsubscriptions.index);
        if (GetElUtil.elementPresence(By.partialLinkText("Customer"))){

            
            WebElement table = GetElUtil.getElement(By.className("cb-bootstrap-table"));
            
            List<WebElement> rows = table.findElements(By.className("cb-bootstrap-table-row"));
            for(int i = 0; i<rows.size();i++){
                WebElement tableLoop = GetElUtil.getElement(By.className("cb-bootstrap-table"));
            
                List<WebElement> rowsLoop = tableLoop.findElements(By.className("cb-bootstrap-table-row"));
                WebElement details = rowsLoop.get(i).findElement(By.className("cb-icon-new-option-redirect"));
                
                details.click();
                
                WebElement configurations = GetElUtil.getElement(By.id("customer-details"));
                String text = configurations.getText();
                if(text.contains("Auto Collection\n" +"On Change")){
                System.out.println("___on___________________");
                }else{
                System.out.println("___off___________________");
                }
                
                UrlHelper.loadURL(Routes.rsubscriptions.index);
                
            }
        }

        
    }
    
    @AfterClass
    void signout() throws Exception{
        LoginHelper.logoutBrowserNEnv();
    }
}
