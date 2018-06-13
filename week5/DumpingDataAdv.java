/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chargebee.qa.app.test;

import com.chargebee.Result;
import com.chargebee.app.models.TimeMachine;
import com.chargebee.models.Customer;
import com.chargebee.qa.core.QATimeMachine;
import static com.chargebee.qa.data.Constants.APR;
import com.chargebee.qa.diff.DemoDataFileDiff;
import com.chargebee.qa.javaclient.JavaClientRunner;
import com.chargebee.qa.util.DumpUtil;
import com.chargebee.qa.util.Fixes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author cb-kiruthika
 */
public class DumpingDataAdv {

    private QATimeMachine t;

    public static void main(String[] args) throws Throwable {
        DumpUtil.throwIfBaseFileNotExists = false;            //Uncomment when running for the 1st time only
        new JavaClientRunner()
                .testThisClass()
                .run();
    }

    @BeforeClass
    public void load() throws Exception {
        t = new QATimeMachine().updateServerTime(true);
        JavaClientRunner.siteSetup();
    }

//    @Test(description = "Testing for docu")
//    public void testCreateCustomer() throws Exception {
//        String content = "This is a test content";
//        String fileNameWithExtension = "test.txt";
//        DumpUtil.dumpAndCheckFile(content, fileNameWithExtension);
//    }

    @Test(description = "Testing for docu")
    public void testCreateCustomer() throws Exception {
        t.gotoo(1, APR, 2013);
        Result result = Customer.create()
                  .firstName("John")
                  .lastName("Doe")
                  .email("john@test.com")
                  .billingAddressFirstName("John")
                  .billingAddressLastName("Doe")
                  .billingAddressLine1("PO Box 9999")
                  .billingAddressCity("Walnut")
                  .billingAddressState("California")
                  .billingAddressZip("91789")
                  .billingAddressCountry("US").request();
        System.out.println(result);
//        Fixes.modifyBaseObjIfNeeded(baseObj, newObj);
        DumpUtil.dumpAndCheck(result, "subscription_create_api");
        t.reset(false);
    }
//    @Test
//    public void testCreateCustomer() throws Exception {
//        t.gotoo(1, APR, 2013);
//
//        String srcPath = "/Users/cb-kiruthika/chargebee-app/qa_data/basedata/dumpfiles/TestCaseAPI/testCreateCustomer/test_base.csv";
//        String destPath = "/Users/cb-kiruthika/chargebee-app/qa_data/newdata/dumpfiles/TestCaseAPI/testCreateCustomer/test_new.csv";
//
//        DemoDataFileDiff.diffData(srcPath, destPath, "csv", true);
//        t.reset(false);
//    }
}







////////---------------------------------------------------________-------------------//
















/*
 * Copyright (c) 2013 ChargeBee Inc
 * All Rights Reserved.
 */
package com.chargebee.qa.util;

import com.chargebee.app.invoices.DocData;
import com.chargebee.app.models.Customer;
import com.chargebee.app.models.CustomerBalance;
import com.chargebee.app.models.Invoice;
import com.chargebee.app.models.NewInvoice;
import static com.chargebee.app.models.Tables.*;
import com.chargebee.app.qa.QAHelper;
import com.chargebee.app.scheduler.jobs.InvoiceData;
import static com.chargebee.framework.util.CSVUtils.toArray;
import static com.chargebee.framework.util.GlobalUtil.*;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.chargebee.qa.javaclient.tests.DiffUtilQA;
import static com.chargebee.qa.util.DumpUtil.hasDiffUpdateBaseData;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.chargebee.framework.JsonElem;
import com.chargebee.framework.metamodel.SegmentConfig;
import com.chargebee.framework.util.GlobalUtil;

/**
 *
 * @author vaibhav
 */
public class Fixes {

    /**
     * If you want to change the existing base data (For Eg: New attribute needs
     * to be added or if a attribute needs changes) then you could use this
     * file.
     *
     * STEPS : 1.) Return 'true' for isModifibale method. This method is used to
     * update base file in DumpUtils.java. 2.) 'modifyBaseObjIfNeeded' method is
     * called from compareObjects method in DumpUtils.java. Write a separate
     * method for changing your baseObjects with necessary checks you need and
     * call it in that method and run test cases to modify the base files. 3.)
     * Once the changes are done revert Fixes.java to keep it clean. If you need
     * your code, comment your code and make sure 'isModifiable' method is
     * returning false.
     */
    public static boolean isModifiable() {
        return true;
    }

    public static void modifyBaseObjIfNeeded(JSONObject baseObj, JSONObject newObj) throws Exception {
        if (!isModifiable()) {
            return;
        }
        testBaseDataFix(baseObj);
        return;
//        addBalanceCurrencyCode(baseObj, newObj);
    }

    private static void testBaseDataFix(JSONObject baseObj) throws Exception {
        if ("customer".equals(baseObj.getString("object"))) {
            baseObj.put("test_field", "0");
            GlobalUtil.log("<<<<<<<<<<<<<<<< Added customer test field >>>>>>>>>>>>>>>>>");
        }
    }

    private static void addPSObj(JSONObject baseObj, JSONObject newObj) throws JSONException {
        String objType = baseObj.optString("object");
        if (objType.equals("event") && baseObj.optString("event_type").equals("payment_source_deleted")) {
            if (!baseObj.getJSONObject("content").has("payment_source") && newObj.getJSONObject("content").has("payment_source")) {
                baseObj.getJSONObject("content").put("payment_source", newObj.getJSONObject("content").getJSONObject("payment_source"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'payment source' attribute in event content object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    private static void addDeletedColumn(JSONObject baseObj, JSONObject newObj) throws JSONException {
        String objType = baseObj.optString("object");
        if (objType.equals("payment_source")) {
            if (!baseObj.has("deleted") && newObj.has("deleted")) {
                baseObj.put("deleted", newObj.getBoolean("deleted"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'deleted' attribute in payment_source object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    private static void addSettledAt(JSONObject baseObj, JSONObject newObj) throws JSONException {
        String objType = baseObj.optString("object");
        if (objType.equals("transaction")) {
            if (!baseObj.has("settled_at") && newObj.has("settled_at")) {
                baseObj.put("settled_at", newObj.getInt("settled_at"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'settled_at' attribute in transaction object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void addBalanceCurrencyCode(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (baseObj.has("balances")) {
                JSONArray balancesBase = baseObj.getJSONArray("balances");
                JSONArray balancesNew = newObj.getJSONArray("balances");
                for (int i = 0; i < balancesBase.length(); i++) {
                    JSONObject baseBal = balancesBase.getJSONObject(i);
                    JSONObject newBal = balancesNew.getJSONObject(i);
                    if (newBal.has("currency_code") && !baseBal.has("currency_code")) {
                        baseBal.put("currency_code", newBal.getString("currency_code"));
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'currency_code' attribute in base customer balances object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                    if (newBal.has("balance_currency_code") && !baseBal.has("balance_currency_code")) {
                        baseBal.put("balance_currency_code", newBal.getString("balance_currency_code"));
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'balance_currency_code' attribute in base customer balances object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
            }
        }
    }

    public static void removeCancelReasonForActiveSub(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("subscription")) {
            if (baseObj.has("cancel_reason") && !newObj.has("cancel_reason")) {
                baseObj.remove("cancel_reason");
            }
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Removed 'cancel_reason' attribute in base subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    public static void addAmountToCollect(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice")) {
            if (newObj.has("amount_to_collect") && !baseObj.has("amount_to_collect")) {
                baseObj.put("amount_to_collect", newObj.getInt("amount_to_collect"));
            }
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'amount_to_collect' attribute in base invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

    }

    public static void modifyNextRetry(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        String next_retry = qinvoices.next_retry.getApiName();
        if (objType.equals("invoice")) {
            if (newObj.has(next_retry) && !baseObj.has(next_retry) && newObj.has("dunning_status") && !baseObj.has("dunning_status")) {
                baseObj.put("dunning_status", newObj.get("dunning_status"));
                baseObj.put(next_retry, newObj.get(next_retry));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'dunning status and next_retry' attribute in base invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void modifyRoundOff(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.equals("credit_note")) {
            if (newObj.has("round_off_amount") && !baseObj.has("round_off_amount")) {
                baseObj.put("round_off_amount", newObj.get("round_off_amount"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'round_off_amount' attribute in base" + objType + "object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateTxnGatewayValue(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("transaction")) {
            if (baseObj.has("gateway") && newObj.has("gateway")) {
                if (baseObj.getString("gateway").equals("not_applicable")
                        && (newObj.getString("gateway").equals("paypal_express_checkout") || newObj.getString("gateway").equals("amazon_payments"))) {
                    baseObj.put("gateway", newObj.get("gateway"));
                }
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Changed 'gateway attribute value to paypal_express_checkout or amazon_payments' in txn object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updatePmGatewayValue(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (baseObj.has("payment_method") && newObj.has("payment_method")) {
                JSONObject basePMObj = baseObj.getJSONObject("payment_method");
                JSONObject newPMObj = newObj.getJSONObject("payment_method");
                if (basePMObj.has("gateway") && newPMObj.has("gateway")) {
                    if (basePMObj.getString("gateway").equals("not_applicable")
                            && (newPMObj.getString("gateway").equals("paypal_express_checkout") || newPMObj.getString("gateway").equals("amazon_payments"))) {
                        basePMObj.put("gateway", newPMObj.get("gateway"));
                    }
                    log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Changed 'gateway attribute value to paypal_express_checkout or amazon_payments' in customer object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            }
        }
    }

    public static void modifyExpiryYear(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        String expiry_year = "expiry_year";
        if (objType.equals("card")) {
            if (baseObj.has(expiry_year) && newObj.has(expiry_year)
                    && baseObj.getInt(expiry_year) <= 2019
                    && newObj.getInt(expiry_year) == 2022) {
                baseObj.put(expiry_year, newObj.get(expiry_year));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Modified valid expiry_year in base credit_card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        } else if (objType.equals("payment_source")) {
            JSONObject bCardObj = baseObj.optJSONObject("card");
            JSONObject nCardObj = newObj.optJSONObject("card");
            if (bCardObj != null && nCardObj != null) {
                if (bCardObj.has(expiry_year) && nCardObj.has(expiry_year)
                        && bCardObj.getInt(expiry_year) <= 2019
                        && nCardObj.getInt(expiry_year) == 2022) {
                    bCardObj.put(expiry_year, nCardObj.get(expiry_year));
                    log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Modified valid expiry_year in base payment_source object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            }
        }
    }

    public static void changeStateAndStateCodeForIndia(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.equals("credit_note") || objType.equals("customer") || objType.equals("estimate") || objType.equals("subscription")) {
            if (baseObj.has("billing_address") && baseObj.getJSONObject("billing_address").has("country") && baseObj.getJSONObject("billing_address").getString("country").equals("IN")) {
                log("<<<<<<<<<<<<<<< changing billing address >>>>>>>>>>>>>>>>>");
                baseObj.put("billing_address", newObj.getJSONObject("billing_address"));
            }
            if (baseObj.has("shipping_address") && baseObj.getJSONObject("shipping_address").has("country") && baseObj.getJSONObject("shipping_address").getString("country").equals("IN")) {
                log("<<<<<<<<<<<<<<< changing shipping address >>>>>>>>>>>>>>>>>");
                baseObj.put("shipping_address", newObj.getJSONObject("shipping_address"));

            }
        }
        if (objType.equals("card")) {
            if (baseObj.has("billing_state") && newObj.has("billing_state_code") && baseObj.has("billing_country") && baseObj.getString("billing_country").equals("IN")) {
                log("<<<<<<<<<<<<<<< changing card billing address >>>>>>>>>>>>>>>>>");
                baseObj.put("billing_state_code", newObj.get("billing_state_code"));
                baseObj.put("billing_state", newObj.get("billing_state"));
            }
        }
    }

    public static void updatePaymentSrcIdForCard(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("card")) {
            if (newObj.has("payment_source_id") && !baseObj.has("payment_source_id")) {
                baseObj.put("payment_source_id", newObj.getString("payment_source_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'payment_source_id' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("gateway_account_id") && !baseObj.has("gateway_account_id")) {
                baseObj.put("gateway_account_id", newObj.getString("gateway_account_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'gateway_account_id' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("funding_type") && !baseObj.has("funding_type")) {
                baseObj.put("funding_type", newObj.getString("funding_type"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'funding_type' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("issuing_country") && !baseObj.has("issuing_country")) {
                baseObj.put("issuing_country", newObj.getString("issuing_country"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'funding_type' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
        if (objType.equals("payment_source")) {
            if (newObj.has("issuing_country") && !baseObj.has("issuing_country")) {
                baseObj.put("issuing_country", newObj.getString("issuing_country"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'funding_type' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }

        if (objType.equals("transaction")) {
            if (newObj.has("fraud_status") && !baseObj.has("fraud_status")) {
                baseObj.put("fraud_status", newObj.getString("fraud_status"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'fraud_status' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("fraud_reason") && !baseObj.has("fraud_reason")) {
                baseObj.put("fraud_reason", newObj.getString("fraud_reason"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'fraud_reason' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void fixMRR(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (!objType.equals("subscription")) {
            return;
        }
        if (objType.equals("subscription")) {
            if (baseObj.has("status") && baseObj.get("status").equals("cancelled")) {
                log("<<<<<<<<<<<<<<< fixing MRR >>>>>>>>>>>>>>>>>");
                baseObj.remove("mrr");
                baseObj.put("mrr", newObj.get("mrr"));
                System.out.println(">>>");
                baseObj.remove("exchange_rate");
                baseObj.remove("base_currency_code");
            }
        }
    }

    public static void populatedUbcDeleted(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("unbilled_charge")) {
            if (newObj.has("deleted") && !baseObj.has("deleted")) {
                log("<<<<<<<<<<<<<<< populating deleted >>>>>>>>>>>>>>>>>");
                baseObj.put("deleted", newObj.getBoolean("deleted"));
            }
        }
        if (objType.equals("estimate")) {
            if (newObj.has("unbilled_charge_estimates")) {
                JSONArray cnNewEstimate = newObj.getJSONArray("unbilled_charge_estimates");
                JSONArray cnOldEstimate = baseObj.getJSONArray("unbilled_charge_estimates");
                for (int i = 0; i < cnNewEstimate.length(); i++) {
                    JSONObject cnNew = cnNewEstimate.getJSONObject(i);
                    JSONObject cnOld = cnOldEstimate.getJSONObject(i);
                    if (cnNew.has("deleted") && !cnOld.has("deleted")) {
                        cnOld.put("deleted", cnNew.getBoolean("deleted"));
                        log("<<<<<<<<<<<<<<< Adding deleted to ubc estimate >>>>>>>>>>>>>>>>>");
                    }
                }

            }
        }
    }

    public static void updateCustomerBalance(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (!baseObj.has("customer_balance") && newObj.has("customer_balance")) {
                baseObj.put("customer_balance", newObj.get("customer_balance"));
                log("<<<<<<<<<<<<<<< Adding customer balance  >>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateInvoiceRecurring(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") && newObj.has("recurring") && baseObj.has("recurring")) {
            if (!newObj.optString("recurring").equals(baseObj.optString("recurring"))) {
                JSONArray lines = newObj.optJSONArray("line_items");
                Boolean isRecurring = false;
                for (int i = 0; i < lines.length(); i++) {
                    JSONObject li = lines.getJSONObject(i);
                    if (li.getLong("date_from") != li.getLong("date_to")) {
                        isRecurring = true;
                        break;
                    }
                }
                if (!isRecurring.equals(baseObj.getBoolean("recurring"))) {
                    baseObj.put("recurring", isRecurring);
                }
            }
        }
    }

    public static void updatePaymentSrcIdForSubs(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("subscription")) {
            if (newObj.has("payment_source_id") && !baseObj.has("payment_source_id")) {
                baseObj.put("payment_source_id", newObj.getString("payment_source_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'payment_source_id' attribute in base card object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }

    }

    public static void updatePaymentSrcIdForCust(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (newObj.has("primary_payment_source_id") && !baseObj.has("primary_payment_source_id")) {
                baseObj.put("primary_payment_source_id", newObj.getString("primary_payment_source_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'primary_payment_source_id' attribute in base customer object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("backup_payment_source_id") && !baseObj.has("backup_payment_source_id")) {
                baseObj.put("backup_payment_source_id", newObj.getString("backup_payment_source_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'backup_payment_source_id' attribute in base customer object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updaeInvoiceNewSalesAmt(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.contains("invoice_estimate") || objType.contains("unbilled_charge_estimate")) {
            if (newObj.has("new_sales_amount") && !baseObj.has("new_sales_amount")) {
                Integer nsamt = newObj.getInt("new_sales_amount");
                Integer iamt = newObj.getInt("total");
                Boolean firstInv = newObj.getBoolean("first_invoice");
                if (firstInv && nsamt.equals(iamt)) {
                    baseObj.put("new_sales_amount", nsamt);
                }
            }
        }
    }

    public static void timeMachineErrorJson(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objectName = baseObj.optString("object");
        if (!("time_machine".equals(objectName))) {
            return;
        }

        if (newObj.has("error_json") && !baseObj.has("error_json")) {
            baseObj.put("error_json", newObj.getString("error_json"));
        }
    }

    public static void updaeFirstInvoiceAttr(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.contains("invoice_estimate") || objType.contains("unbilled_charge_estimate")) {
            Boolean nfn = newObj.getBoolean("first_invoice");
            Boolean bfn = baseObj.getBoolean("first_invoice");
            if (nfn != null && bfn != null && !nfn.equals(bfn)) {
                Invoice inv = qinvoices.number.dbFetchOne(newObj.getString("id"));
                NewInvoice newInv = qnew_invoices.invoice_id.dbFetchOne(inv == null ? 0l : inv.id());
                if (newInv != null) {
                    if (!bfn) { // base - false
                        baseObj.put("first_invoice", nfn);
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'first' attribute in invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                } else if (bfn) {
                    baseObj.put("first_invoice", nfn);
                    log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'first' attribute in invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            }
        }
    }

    public static void updateTxnPaymentSourceID(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("transaction")) {
            if (newObj.has("payment_source_id") && !baseObj.has("payment_source_id")) {
                baseObj.put("payment_source_id", newObj.get("payment_source_id"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'payment_source_id' attribute in txn object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateCustomerBalanceChanges(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (newObj.has("unbilled_charges") && !baseObj.has("unbilled_charges")) {
                baseObj.put("unbilled_charges", newObj.get("unbilled_charges"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'unbilled_charges' attribute in customer object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateLineDiscounts(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.equals("credit_note")) {
            if (newObj.has("line_item_discounts") && !baseObj.has("line_item_discounts")) {
                baseObj.put("line_item_discounts", newObj.getJSONArray("line_item_discounts"));
                log("<<<<<<<<<<<<<<< Adding line_item_discounts object >>>>>>>>>>>>>>>>>");
            }
        }

        if (objType.equals("estimate")) {
            if (newObj.has("invoice_estimate")) {
                JSONObject invEstimate = newObj.getJSONObject("invoice_estimate");
                if (invEstimate.has("line_item_discounts") && !baseObj.getJSONObject("invoice_estimate").has("line_item_discounts")) {
                    baseObj.getJSONObject("invoice_estimate").put("line_item_discounts", invEstimate.getJSONArray("line_item_discounts"));
                    log("<<<<<<<<<<<<<<< Adding line_item_discounts object >>>>>>>>>>>>>>>>>");
                }
            }

            if (newObj.has("next_invoice_estimate")) {
                JSONObject invEstimate = newObj.getJSONObject("next_invoice_estimate");
                if (invEstimate.has("line_item_discounts") && !baseObj.getJSONObject("next_invoice_estimate").has("line_item_discounts")) {
                    baseObj.getJSONObject("next_invoice_estimate").put("line_item_discounts", invEstimate.getJSONArray("line_item_discounts"));
                    log("<<<<<<<<<<<<<<< Adding line_item_discounts object >>>>>>>>>>>>>>>>>");
                }
            }

            if (newObj.has("credit_note_estimates")) {
                JSONArray cnNewEstimate = newObj.getJSONArray("credit_note_estimates");
                JSONArray cnOldEstimate = baseObj.getJSONArray("credit_note_estimates");
                for (int i = 0; i < cnNewEstimate.length(); i++) {
                    JSONObject cnNew = cnNewEstimate.getJSONObject(i);
                    JSONObject cnOld = cnOldEstimate.getJSONObject(i);
                    if (cnNew.has("line_item_discounts") && !cnOld.has("line_item_discounts")) {
                        cnOld.put("line_item_discounts", cnNew.getJSONArray("line_item_discounts"));
                        log("<<<<<<<<<<<<<<< Adding line_item_discounts object >>>>>>>>>>>>>>>>>");
                    }
                }

            }
        }
    }

    public static void updateSetupCost(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("subscription")) {
            if (newObj.has("setup_fee") && !baseObj.has("setup_fee")) {
                baseObj.put("setup_fee", newObj.get("setup_fee"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "Added 'setup_fee' attribute in base subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateSubscriptionReferralData(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("subscription")) {
            if (baseObj.has("referral_campaign")) {
                baseObj.remove("referral_campaign");
                baseObj.put("referral_campaign", newObj.getJSONObject("referral_campaign"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<< Modified baseObj for Referral campaign object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    private static JSONObject estmUpdate(JSONObject baseObj, JSONObject newObj, String key) throws Exception {
        if (baseObj.has(key) && baseObj.getJSONObject(key).has("line_items")) {
            for (int i = 0; i < baseObj.getJSONObject(key).getJSONArray("line_items").length(); i++) {
                if (i >= newObj.getJSONObject(key).getJSONArray("line_items").length()) {
                    continue;
                }
                if (newObj.getJSONObject(key).getJSONArray("line_items").getJSONObject(i).has("subscription_id") && !baseObj.getJSONObject(key).getJSONArray("line_items").getJSONObject(i).has("subscription_id")) {
                    baseObj.getJSONObject(key).getJSONArray("line_items").getJSONObject(i).put("subscription_id", newObj.getJSONObject(key).getJSONArray("line_items").getJSONObject(i).get("subscription_id"));
                    log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'subscription_id' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            }
        }
        return baseObj;
    }

    private static JSONObject updateEsmArray(JSONObject baseObj, JSONObject newObj, String key) throws Exception {
        if (baseObj.has(key)) {
            JSONArray jsonArray = baseObj.getJSONArray(key);
            JSONArray newArray = newObj.getJSONArray(key);
            for (int l = 0; l < jsonArray.length(); l++) {
                for (int i = 0; i < jsonArray.getJSONObject(l).getJSONArray("line_items").length(); i++) {
                    if (newArray.getJSONObject(l).getJSONArray("line_items").getJSONObject(i).has("subscription_id") && !jsonArray.getJSONObject(l).getJSONArray("line_items").getJSONObject(i).has("subscription_id")) {
                        jsonArray.getJSONObject(l).getJSONArray("line_items").getJSONObject(i).put("subscription_id", newArray.getJSONObject(l).getJSONArray("line_items").getJSONObject(i).get("subscription_id"));
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'subscription_id' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
            }
            baseObj.put(key, jsonArray);
        }
        return baseObj;
    }

    public static void updateLineItemSubId(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        String invoice_estimate = "invoice_estimate";
        String next_inv_estimate = "next_invoice_estimate";
        String cn_estimate = "credit_note_estimate";
        String ubc_estimate = "unbilled_charges_estimate";

        boolean isEtm = objType.equals("estimate");
        boolean estmObj = isEtm && baseObj.has(invoice_estimate) || baseObj.has(cn_estimate)
                || baseObj.has(ubc_estimate) || baseObj.has(next_inv_estimate) || baseObj.has("invoice_estimates") || baseObj.has("credit_note_estimates");
        if (estmObj) {
            estmUpdate(baseObj, newObj, invoice_estimate);
            estmUpdate(baseObj, newObj, cn_estimate);
            estmUpdate(baseObj, newObj, ubc_estimate);
            estmUpdate(baseObj, newObj, next_inv_estimate);
            updateEsmArray(baseObj, newObj, "invoice_estimates");
            updateEsmArray(baseObj, newObj, "credit_note_estimates");
        } else if (objType.equals("invoice") || objType.equals("credit_note")) {
            if (baseObj.has("line_items")) {
                for (int i = 0; i < baseObj.getJSONArray("line_items").length(); i++) {
                    if (newObj.getJSONArray("line_items").optJSONObject(i) == null) {
                        continue;
                    }
                    if (newObj.getJSONArray("line_items").getJSONObject(i).has("subscription_id") && !baseObj.getJSONArray("line_items").getJSONObject(i).has("subscription_id")) {
                        baseObj.getJSONArray("line_items").getJSONObject(i).put("subscription_id", newObj.getJSONArray("line_items").getJSONObject(i).get("subscription_id"));
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'subscription_id' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
            }
        }
    }

    public static void updateTaxRelatedField(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("invoice") || objType.equals("credit_note") || objType.equals("estimate")) {
            if (objType.equals("estimate")) {
                if (baseObj.has("invoice_estimate") && baseObj.getJSONObject("invoice_estimate").has("line_items")) {

                    for (int i = 0; i < baseObj.getJSONObject("invoice_estimate").getJSONArray("line_items").length(); i++) {
                        if (newObj.getJSONObject("invoice_estimate").getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason") && !baseObj.getJSONObject("invoice_estimate").getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason")) {
                            baseObj.getJSONObject("invoice_estimate").getJSONArray("line_items").getJSONObject(i).put("tax_exempt_reason", newObj.getJSONObject("invoice_estimate").getJSONArray("line_items").getJSONObject(i).get("tax_exempt_reason"));
                            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'tax_exempt_reason' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        }
                    }

                }
                if (baseObj.has("next_invoice_estimate") && baseObj.getJSONObject("next_invoice_estimate").has("line_items")) {

                    for (int i = 0; i < baseObj.getJSONObject("next_invoice_estimate").getJSONArray("line_items").length(); i++) {
                        if (newObj.getJSONObject("next_invoice_estimate").getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason") && !baseObj.getJSONObject("next_invoice_estimate").getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason")) {
                            baseObj.getJSONObject("next_invoice_estimate").getJSONArray("line_items").getJSONObject(i).put("tax_exempt_reason", newObj.getJSONObject("next_invoice_estimate").getJSONArray("line_items").getJSONObject(i).get("tax_exempt_reason"));
                            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'tax_exempt_reason' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        }
                    }
                }
            }
            if (baseObj.has("line_items")) {
                for (int i = 0; i < baseObj.getJSONArray("line_items").length(); i++) {
                    if (newObj.getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason") && !baseObj.getJSONArray("line_items").getJSONObject(i).has("tax_exempt_reason")) {
                        baseObj.getJSONArray("line_items").getJSONObject(i).put("tax_exempt_reason", newObj.getJSONArray("line_items").getJSONObject(i).get("tax_exempt_reason"));
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'tax_exempt_reason' attribute in line item object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
            }

            if (newObj.has("is_vat_moss_registered") && !baseObj.has("is_vat_moss_registered")) {
                baseObj.put("is_vat_moss_registered", newObj.get("is_vat_moss_registered"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'is_vat_moss_registered' attribute in invoice/credit_note object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }

            if (newObj.has("is_digital") && !baseObj.has("is_digital")) {
                baseObj.put("is_digital", newObj.get("is_digital"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'is_digital' attribute in invoice/credit_note object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

//    public static void updateVoidedAtParam(JSONObject baseObj, JSONObject newObj) throws JSONException {
//        String objectName = baseObj.optString("object");
//        if (!("invoice".equals(objectName) || "credit_note".equals(objectName))) {
//            return;
//        }
//
//        if (newObj.has("voided_at") && !baseObj.has("voided_at")) {
//            baseObj.put("voided_at", newObj.get("voided_at"));
//        }
//    }
    /**
     * This method is to update base data for csv files.
     */
    public static void modifyBaseObjIfNeeded(File oldFile, List<CSVRecord> oldFileRecs, List<CSVRecord> newFileRecs) throws Exception {
        if (!isModifiable()) {
            return;
        }
        //A sample method is given below.
        addColToCsv(oldFile, oldFileRecs, newFileRecs);
    }

    public static void addColToCsv(File oldFile, List<CSVRecord> oldFileRecs, List<CSVRecord> newFileRecs) throws Exception {
        if (!oldFile.getName().equals("InvoiceProperties.csv")) {
            return;
        }
        File newFixFile = new File(oldFile.getAbsolutePath());
        Files.delete(oldFile.toPath());
        Files.createFile(newFixFile.toPath());
        List<String> lines = new ArrayList();
        int maxSize = oldFileRecs.size() > newFileRecs.size() ? oldFileRecs.size() : newFileRecs.size();
        for (int i = 0; i < maxSize; i++) {
            String[] oldRec = i < oldFileRecs.size() ? toArray(oldFileRecs.get(i)) : null;
            String[] newRec = i < newFileRecs.size() ? toArray(newFileRecs.get(i)) : null;
            if (oldRec != null && newRec != null) {
                int newColsSize = newRec.length - oldRec.length;
                if (isModifiable() && hasDiffUpdateBaseData && oldRec.length < newRec.length) {
                    String[] fixRec = new String[newRec.length];
                    fixRec = ArrayUtils.clone(oldRec);
                    for (int addedExtraCol = 0; addedExtraCol < newColsSize; addedExtraCol++) {
                        fixRec = ArrayUtils.add(fixRec, newRec[oldRec.length + addedExtraCol]);
                    }
                    lines.add(appendString(fixRec));
                }
            }
        }
        FileUtils.writeLines(newFixFile, lines);
    }

    private static void updateJurisForCountry(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objectName = baseObj.optString("object");
        if (!("credit_note".equals(objectName) || "invoice".equals(objectName)
                || "invoice_estimate".equals(objectName) || "estimate".equals(objectName))) {
            return;
        }

        if ("estimate".equals(objectName) && baseObj.has("next_invoice_estimate")) {
            baseObj = baseObj.getJSONObject("next_invoice_estimate");
            newObj = newObj.getJSONObject("next_invoice_estimate");
        }

        if ("estimate".equals(objectName) && baseObj.has("invoice_estimate")) {
            baseObj = baseObj.getJSONObject("invoice_estimate");
            newObj = newObj.getJSONObject("invoice_estimate");
        }
        if (newObj.has("line_item_taxes") && baseObj.has("line_item_taxes")) {
            //mod the base data
            JSONArray oLits = baseObj.getJSONArray("line_item_taxes");
            JSONArray nLits = newObj.getJSONArray("line_item_taxes");
            System.out.println(nLits.toString());
            System.out.println(oLits.toString());
            if (nLits.length() != oLits.length()) {
                return;
            }
            for (int i = 0; i < nLits.length(); i++) {
                JSONObject nLit = nLits.getJSONObject(i);
                JSONObject oLit = oLits.getJSONObject(i);
                if (nLit.get("tax_amount").equals(oLit.getInt("tax_amount"))
                        && nLit.get("tax_name").equals(oLit.getString("tax_name"))
                        && nLit.get("tax_rate").equals(oLit.get("tax_rate"))) {
                    if (oLit.optString("tax_juris_type").equals("")
                            && oLit.optString("tax_juris_code").equals("")
                            && oLit.optString("tax_juris_name").equals("")) {
                        //same
                        oLit.put("tax_juris_type", nLit.opt("tax_juris_type"));
                        oLit.put("tax_juris_code", nLit.opt("tax_juris_code"));
                        oLit.put("tax_juris_name", nLit.opt("tax_juris_name"));
                    }
                }
            }
        }
    }

    public static void updateAdvInvoicingFields(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");

        if (objType.equals("invoice")) {
            if (newObj.has("has_advance_charges") && !baseObj.has("has_advance_charges")) {
                baseObj.put("has_advance_charges", newObj.get("has_advance_charges"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'has_advance_charges' attribute in invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }

        if (objType.equals("subscription")) {
            if (newObj.has("next_billing_at") && !baseObj.has("next_billing_at")) {
                String st = baseObj.getString("status");
                Object nextBillingAt;
                switch (st) {
                    case "non_renewing":
                    case "active":
                        nextBillingAt = baseObj.get("current_term_end");
                        break;
                    case "in_trial":
                        nextBillingAt = baseObj.get("trial_end");
                        break;
                    case "future":
                        nextBillingAt = baseObj.opt("trial_end") != null ? baseObj.get("trial_end") : baseObj.get("start_date");
                        break;
                    default:
                        throw new RuntimeException("Not handled " + st);
                }
                baseObj.put("next_billing_at", nextBillingAt);
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'next_billing_at' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (baseObj.has("next_billing_at") && !newObj.has("next_billing_at")) {//for fixing wrongly pushed data
                String st = baseObj.getString("status");
                if (!st.contains("in_trial") && !st.contains("future") && !st.contains("active")) {
                    baseObj.remove("next_billing_at");
                }
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Removed 'next_billing_at' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public static void updateForNetTermDays(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("customer")) {
            if (newObj.has("net_term_days") && !baseObj.has("net_term_days")) {
                baseObj.put("net_term_days", newObj.get("net_term_days"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'net_term_days' attribute in customer object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
        if (objType.equals("invoice")) {
            if (newObj.has("net_term_days") && !baseObj.has("net_term_days")) {
                baseObj.put("net_term_days", newObj.get("net_term_days"));
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'net_term_days' attribute in invoice object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (newObj.has("due_date") && !baseObj.has("due_date")) {
                if (newObj.getString("due_date").equals(newObj.getString("date"))) {
                    baseObj.put("due_date", baseObj.get("date"));
                } else {
                    baseObj.put("due_date", newObj.get("due_date"));
                }
            }
        }
    }

    public static void updatePriceOverride(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");
        if (objType.equals("subscription")) {
            updateSubAttr(baseObj, newObj);
        }
        if (objType.equals("hosted_page") && newObj.has("content")) {
            JSONObject content = newObj.getJSONObject("content");
            if (content.has("subscription")) {
                updateSubAttr(baseObj, newObj);
            }
        }
    }

    public static void updateSrcForDelEvent(JSONObject baseObj, JSONObject newObj) throws Exception {
        String objType = baseObj.optString("object");

        if (objType.equals("event")) {
            baseObj.put("source", newObj.get("source"));
            if (newObj.has("user")) {
                baseObj.put("user", newObj.get("user"));
            }
        }
    }

    public static JSONObject invUpdatedEvent(JSONObject jsonBaseObj, JSONObject jsonDumpObj) throws Exception {
        String listKey = "list";
        if (!jsonBaseObj.has(listKey)) {
            return jsonBaseObj;
        }
        if (!(jsonBaseObj.get(listKey) instanceof JSONArray)) {
            return jsonBaseObj;
        }
        JSONArray baseArr = (JSONArray) jsonBaseObj.get(listKey);
        JSONArray newArr = (JSONArray) jsonDumpObj.get(listKey);
        if (baseArr.length() == newArr.length()) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< same length arrays");
            return jsonBaseObj;
        }
        if (!baseArr.getJSONObject(0).has("event")) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< not event arrays");
            return jsonBaseObj;
        }
        int maxIdx = baseArr.length();
        for (int i = 0; i < newArr.length(); i++) {
            JsonElem el = new JsonElem(newArr.getString(i));
            if (el.optNode("event") == null) {
                return jsonBaseObj;
            }
            String newEventType = el.node("event").str("event_type");
            if (newEventType.equals("invoice_updated")) {
                JsonElem invoice = el.node("event").node("content").node("invoice");
                if (!(invoice.str("status").equals("not_paid") && invoice.str("dunning_status").equals("exhausted"))) {
                    return jsonBaseObj;
                }
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + newEventType + " Added");
                int currIdx = maxIdx;
                while (currIdx != i) {
                    baseArr.put(currIdx, baseArr.get(currIdx - 1));
                    currIdx -= 1;
                }
                baseArr.put(i, newArr.get(i));
                maxIdx += 1;
            }
        }
        jsonBaseObj.put(listKey, baseArr);
        return new JSONObject(jsonBaseObj.toString());
    }

    public static JSONObject subCancelledEvent(JSONObject jsonBaseObj, JSONObject jsonDumpObj) throws Exception {
        String listKey = "list";
        if (!jsonBaseObj.has(listKey)) {
            return jsonBaseObj;
        }
        if (!(jsonBaseObj.get(listKey) instanceof JSONArray)) {
            return jsonBaseObj;
        }
        JSONArray baseArr = (JSONArray) jsonBaseObj.get(listKey);
        JSONArray newArr = (JSONArray) jsonDumpObj.get(listKey);
        int maxIdx = baseArr.length();
        for (int i = 0; i < newArr.length(); i++) {
            JsonElem el = new JsonElem(newArr.getString(i));
            if (el.optNode("event") == null) {
                return jsonBaseObj;
            }
            String newEventType = el.node("event").str("event_type");
            if (newEventType.equals("subscription_cancelled")) {
                JSONObject cancelledEvent = newArr.getJSONObject(i);
                JSONObject baseCancelledEvent = baseArr.getJSONObject(i);
                if (!baseCancelledEvent.getJSONObject("event").has("invoice")) {
                    baseArr.put(i, cancelledEvent);
                }
            }
        }
        jsonBaseObj.put(listKey, baseArr);
        return new JSONObject(jsonBaseObj.toString());
    }

    public static JSONObject removeEvent(JSONObject jsonBaseObj, JSONObject jsonDumpObj) throws Exception {
        String listKey = "list";
        if (!jsonBaseObj.has(listKey)) {
            return jsonBaseObj;
        }
        if (!(jsonBaseObj.get(listKey) instanceof JSONArray)) {
            return jsonBaseObj;
        }
        JSONArray baseArr = (JSONArray) jsonBaseObj.get(listKey);
        JSONArray baseArrTemp = new JSONArray();
        JSONArray newArr = (JSONArray) jsonDumpObj.get(listKey);
        if (baseArr.length() == newArr.length()) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< same length arrays");
            return jsonBaseObj;
        }
        if (!baseArr.getJSONObject(0).has("event")) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< not event arrays");
            return jsonBaseObj;
        }
        int count = 0;
        for (int i = 0; i < baseArr.length(); i++) {
            JsonElem el = new JsonElem(baseArr.getString(i));
            if (el.optNode("event") == null) {
                return jsonBaseObj;
            }
            String baseEventType = el.node("event").str("event_type");
            if (el.node("event").optStr("api_version") != null && el.node("event").str("api_version").equals("v2")) {
                return jsonBaseObj;
            }
            if (baseEventType.equals("subscription_changes_scheduled")
                    || baseEventType.equals("subscription_scheduled_changes_removed")
                    || baseEventType.equals("pending_invoice_updated")) {
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + baseEventType + " Removed");
            } else {
                baseArrTemp.put(count, baseArr.get(i));
                count++;
            }
        }
        jsonBaseObj.put(listKey, baseArrTemp);
        return new JSONObject(jsonBaseObj.toString());
    }

    public static JSONObject addNewEvent(JSONObject jsonBaseObj, JSONObject jsonDumpObj) throws Exception {
        String listKey = "list";
        if (!jsonBaseObj.has(listKey)) {
            return jsonBaseObj;
        }
        if (!(jsonBaseObj.get(listKey) instanceof JSONArray)) {
            return jsonBaseObj;
        }
        JSONArray baseArr = (JSONArray) jsonBaseObj.get(listKey);
        JSONArray newArr = (JSONArray) jsonDumpObj.get(listKey);
        if (baseArr.length() == newArr.length()) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< same length arrays");
            return jsonBaseObj;
        }
        if (!baseArr.getJSONObject(0).has("event")) {
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<< not event arrays");
            return jsonBaseObj;
        }
        int maxIdx = baseArr.length();
        for (int i = 0; i < newArr.length(); i++) {
            JsonElem el = new JsonElem(newArr.getString(i));
            if (el.optNode("event") == null) {
                return jsonBaseObj;
            }
            String newEventType = el.node("event").str("event_type");
            if (newEventType.equals("subscription_changes_scheduled")
                    || newEventType.equals("subscription_scheduled_changes_removed")
                    || newEventType.equals("pending_invoice_updated")) {
                log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + newEventType + " Added");
                int currIdx = maxIdx;
                while (currIdx != i) {
                    baseArr.put(currIdx, baseArr.get(currIdx - 1));
                    currIdx -= 1;
                }
                baseArr.put(i, newArr.get(i));
                maxIdx += 1;
            }
        }
        jsonBaseObj.put(listKey, baseArr);
        return new JSONObject(jsonBaseObj.toString());
    }

    public static void updateSubAttr(JSONObject baseObj, JSONObject newObj) throws Exception {
        if (newObj.has("plan_unit_price") && !baseObj.has("plan_unit_price")) {
            Object val = baseObj.has("plan_unit_amount") ? baseObj.get("plan_unit_amount") : null;
            baseObj.put("plan_unit_price", val != null ? val : newObj.get("plan_unit_price"));
            if (val != null) {
                baseObj.remove("plan_unit_amount");
            }
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + val == null ? "Added" : "Replaced" + "plan_unit_price' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if (newObj.has("setup_fee") && !baseObj.has("setup_fee")) {
            Object val = baseObj.has("setup_cost") ? baseObj.get("setup_cost") : null;
            baseObj.put("setup_fee", val != null ? val : newObj.get("setup_fee"));
            if (val != null) {
                baseObj.remove("setup_cost");
            }
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + val == null ? "Added" : "Replaced" + "setup_fee' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if (newObj.has("billing_period_unit") && !baseObj.has("billing_period_unit")) {
            baseObj.put("billing_period_unit", newObj.get("billing_period_unit"));
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'billing_period_unit' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if (newObj.has("billing_period") && !baseObj.has("billing_period")) {
            baseObj.put("billing_period", newObj.get("billing_period"));
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<Added 'billing_period' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if (newObj.has("plan_free_quantity") && !baseObj.has("plan_free_quantity")) {
            Object val = baseObj.has("free_quantity") ? baseObj.get("free_quantity") : null;
            baseObj.put("plan_free_quantity", val != null ? val : newObj.get("plan_free_quantity"));
            if (val != null) {
                baseObj.remove("free_quantity");
            }
            log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + val == null ? "Added" : "Replaced" + "plan_free_quantity' attribute in subscription object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        JSONArray baseObjAddons = baseObj.optJSONArray("addons");
        JSONArray newObjAddons = newObj.optJSONArray("addons");
        if (newObjAddons == null || baseObjAddons == null) {
            return;
        }
        for (int i = 0; i < newObjAddons.length(); i++) {
            JSONObject newObjAddon = newObjAddons.optJSONObject(i);
            if (newObjAddon.has("unit_price")) {
                for (int j = 0; j < baseObjAddons.length(); j++) {
                    JSONObject baseObjAddon = baseObjAddons.optJSONObject(j);
                    if (baseObjAddon.has("id")
                            && baseObjAddon.getString("id").equals(newObjAddon.getString("id"))
                            && !baseObjAddon.has("unit_price")) {
                        Object val = baseObjAddon.has("unit_amount") ? baseObjAddon.get("unit_amount") : null;
                        baseObjAddon.put("unit_price", val != null ? val : newObjAddon.get("unit_price"));
                        if (val != null) {
                            baseObjAddon.remove("unit_amount");
                        }
                        log("<<<<<<<<<<<<<<<<<<<<<<<<<<<" + val == null ? "Added" : "Replaced" + " unit_price attribute in subscription addons object >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
            }
        }
    }

//    public static void updateMRR(JSONObject baseObj, JSONObject newObj) throws JSONException {
//        String objectName = baseObj.optString("object");
//        if (!("subscription".equals(objectName))) {
//            return;
//        }
//
//        if (newObj.has("mrr") && !baseObj.has("mrr")) {
//            baseObj.put("mrr", newObj.get("mrr"));
//        }
//
//        if (newObj.has("exchange_rate") && !baseObj.has("exchange_rate")) {
//            baseObj.put("exchange_rate", newObj.get("exchange_rate"));
//        }
//
//        if (newObj.has("base_currency_code") && !baseObj.has("base_currency_code")) {
//            baseObj.put("base_currency_code", newObj.get("base_currency_code"));
//        }
//    }
    public static void updateDeletedParam(JSONObject baseObj, JSONObject newObj) throws JSONException {
        String objectName = baseObj.optString("object");
        if (!("subscription".equals(objectName) || "customer".equals(objectName)
                || "invoice".equals(objectName) || "transaction".equals(objectName)
                || "credit_note".equals(objectName))) {
            return;
        }

        if (newObj.has("deleted") && !baseObj.has("deleted")) {
            baseObj.put("deleted", newObj.get("deleted"));
        }
    }

    private static void addVATandVoidedColumn(File oldFile, List<CSVRecord> oldFileRecs, List<CSVRecord> newFileRecs) throws Exception {
        if (!oldFile.getName().equals("Invoices.csv")) {
            return;
        }
        File newFixFile = new File(oldFile.getAbsolutePath());
        Files.delete(oldFile.toPath());
        Files.createFile(newFixFile.toPath());
        List<String> lines = new ArrayList();
        int maxSize = oldFileRecs.size() > newFileRecs.size() ? oldFileRecs.size() : newFileRecs.size();

        for (int i = 0; i < maxSize; i++) {
            String[] oldRec = i < oldFileRecs.size() ? toArray(oldFileRecs.get(i)) : null;
            String[] newRec = i < newFileRecs.size() ? toArray(newFileRecs.get(i)) : null;
            if (oldRec != null) {
                if (isModifiable() && hasDiffUpdateBaseData && oldRec.length < newRec.length) {
                    String[] fixRec = ArrayUtils.add(oldRec, newRec[newRec.length - 1]);
                    lines.add(appendString(fixRec));
                } else {
                    lines.add(appendString(oldRec));
                }
            }
        }
        FileUtils.writeLines(newFixFile, lines);
    }

    private static String appendString(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (values.length - 1); i++) {
            if (values != null) {
                sb.append("\"" + values[i] + "\",");
            } else {
                sb.append("\"\",");
            }
        }
        sb.append("\"" + values[values.length - 1] + "\"");
        return sb.toString();
    }

    //This method is used in Dumputil line 386
    public static void assertForLineDiscounts(JSONObject newObj) throws Exception {
        if (DiffUtilQA.class.getSimpleName().equalsIgnoreCase(DumpUtil.testCaseName)) {
            return;
        }
        if (!newObj.getString("object").equalsIgnoreCase("invoice")) {
            return;
        }
        String invNum = newObj.getString("id");
        DocData.LineDiscountCheck res = QAHelper.assertForLineDiscounts(invNum);
        if (res == null) {
            return;
        }
        switch (res.discountAmountStatus()) {
            case NOT_SET:
                dumpToFile(res, invNum);
            case SET:
                dumpToFile(res, invNum);
//                GlobalUtil.assert_(res.deltaTotal() == 0, "Delta Total != 0");
//                GlobalUtil.assert_(res.deltaCnt() == 0, "Delta Count != 0");
                break;
            case PARTIALLY_SET:
                dumpToFile(res, invNum);
//                throw new RuntimeException("not expected!!");
            default:
                throw new RuntimeException("not expected!!");
        }
    }

    //This method os ised in Abstract Test runner line 212
    public static void cleanupLineDiscDiff() {
        File f = new File(lineDiscountDiffFilePath);
        if (f.exists() && f.isFile()) {
            FileUtils.deleteQuietly(f);
        }
    }

    //--- START - Used to dump the internal model
    private static String lineDiscountDiffFilePath = "test-output/dump/line_discount_check.csv";

    private static void dumpToFile(InvoiceData.LineDiscountCheck res, String invId) throws Exception {
        File file = new File(lineDiscountDiffFilePath);
        if (!file.exists()) {
            FileUtils.writeStringToFile(file, csv("test_case_class", "test_case_method", "inv_id", "disc_amt_status", "delta_total", "delta_count") + "\n", true);
        }
        String csv = csv(DumpUtil.testCaseName, DumpUtil.testCaseMethodName, invId, res.discountAmountStatus(), res.deltaTotal(), res.deltaCnt());
        FileUtils.writeStringToFile(file, csv + "\n", true);
    }

    //--- END - Used to dump the internal model
}















//-------------------------------------------------------------////









/*
 * Copyright (c) 2011 chargebee.com
 * All Rights Reserved.
 */
package com.chargebee.qa.util;

import com.chargebee.framework.requests.RequestDetails;
import com.chargebee.framework.requests.RequestSource;
import com.chargebee.app.InitializerServlet;
//import com.chargebee.app.api.gen.ApiCodeGen;
import com.chargebee.framework.api.ApiVersion;
import com.chargebee.framework.api.ApiVersionConfig;
import com.chargebee.qa.core.QATimeMachine;
import com.chargebee.internal.ClazzUtil;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONObject;
import com.chargebee.qa.core.SeleniumBase;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import static com.chargebee.framework.util.GlobalUtil.*;
import com.chargebee.internal.Resource;
import static com.chargebee.qa.core.extensions.QaExtensionPoints.apiCodeGenExtension;
import static com.chargebee.qa.core.extensions.QaExtensionPoints.copyDataHelperCheck;
import com.chargebee.qa.core.internal.CBTestListener;
import com.chargebee.qa.core.internal.DiffAssertException;
import static com.chargebee.qa.core.extensions.QaExtensionPoints.fixesIsModifiabe;
import static com.chargebee.qa.core.extensions.QaExtensionPoints.fixesModifyBaseObjectIfNeeded;
//import com.chargebee.qa.javaclient.tests.util.CopyDataHelper;
import com.chargebee.plugin.CBPluginMgr;
import static com.chargebee.qa.core.extensions.QaExtensionPoints.assertForLineDiscountsExtension;
import com.chargebee.qa.diff.DemoDataFileDiff;
import com.chargebee.qa.diff.DiffTool;
import java.io.FileWriter;
import java.util.concurrent.Callable;

import com.chargebee.qa.util.Fixes;


import org.testng.util.Strings;

/**
 * This is used to dump the response json into files. It generates the new
 * response file every time a method is called. BaseData file is generated only
 * once at the first execution. It dumps the response at two file locations -
 * basedata and newdata. This will dump the json response in base data and new
 * data under chargebee-app/qa with the respective paths. The path for create
 * subscription response 'create_sub.json' for createSubscriptionWithCoupon test
 * case under CouponTest will be like:
 * <ul>
 * <li>base data -
 * chargebee-app/qa/../qa_data/basedata/CouponTest/createSubscriptionWithCoupon/create_sub.json</li>
 * <li>new data -
 * chargebee-app/qa/../qa_data/newdata/CouponTest/createSubscriptionWithCoupon/create_sub.json</li>
 * </ul>
 * <p>
 * This can be used for compare the base data files with new data files at the
 * point of time to test the difference between base data and new data files.
 * <p>
 */
public class DumpUtil {

    /**
     * Currently running test case file name
     */
    public static String testCaseName;
    /**
     * Currently running test case method name
     */
    public static String testCaseMethodName;
    
    /**
     * It is public.So override it in your own main method. Don't change it
     * here.
     */
    public static boolean throwIfBaseFileNotExists = true;
    
    public static boolean dumpBaseFiles = true;
    
    /**
     * It is public.So override it in your own main method. Don't change it
     * here.
     */
    public static boolean hasDiffUpdateBaseData = false;
    
    public static boolean dumpV2Events = true;
    
    public static File dumpDataFile = new File(System.getProperty("user.home")+"/work/chargebee-app/qa/test-output/dump.txt");
    
    /**
     * Currently running Java client version 
     * - null in case of non java client test cases
     */
    public static String javaClientVersion  = null;
    /**
     * Currently running test case name.
     *
     * @return currently running test case name e.g.
     * CouponTest.createSubscriptionWithCoupon
     */
    public static String currentTestCase(){
        return testCaseName + "." + testCaseMethodName;
    }
    
    public static String currentTestCaseWithApiVersion(){
        if (Strings.isNullOrEmpty(javaClientVersion)) {
            return currentTestCase();
        }
        return testCaseName+"-"+javaClientVersion+"."+testCaseMethodName;
    }
    
    // todo: remove this. 
    public static void dump(Object obj, String grpName, String testName) {
        File file = getSuitableFile("test-output/dump/" + grpName + "/" + testName);
        try {
            FileUtils.writeStringToFile(file, obj.toString());
        } catch(IOException exp) {
            throw asRtExp(exp);
        }
    }
    
    /**
     * Used to compare the content other than JSON files . This will create the
     * new file every time called.
     *
     * @param content string content to write in base file and new file.
     * @param fileNameWithExtn name of base file and new file with extension.
     * e.g. example.txt
     * @throws Exception if content mis-match in base file and new file or if
     * base file is not present. Creates the base file as well.
     */
    public static void dumpAndCheckFile(String content, String fileNameWithExtn) throws Exception {
        File baseFile = getBaseFile(fileNameWithExtn);

        File newFile = getNewFile(fileNameWithExtn);

        if(RequestDetails.get(false) != null
                && RequestDetails.get().source() == RequestSource.SCHEDULED_JOB) {
            if(!baseFile.isFile()){
                //Trying a different version.
                System.out.println(String.format("Time machine change - base file not found %s", baseFile.getAbsolutePath()));
                Callable<Pair<File, File>> callable = ApiVersionConfig.wrap(ApiVersionConfig.isVersion(ApiVersion.V2)?
                        ApiVersion.V1:ApiVersion.V2, ()->{
                    return new Pair(getBaseFile(fileNameWithExtn),
                            getNewFile(fileNameWithExtn));
                });
                Pair<File, File> files = callable.call();
                baseFile = files.getKey();
                newFile = files.getValue();
            }
        }

        FileUtils.writeStringToFile(newFile, content);
        if(!baseFile.isFile()) { // basedata not present - dump it
            FileUtils.writeStringToFile(baseFile, content);
            if(throwIfBaseFileNotExists){
                throw new DiffException("Base file not present " + baseFile.getAbsolutePath() + ". Hence writing");
            }
        } else if (!FileUtils.contentEquals(baseFile, newFile)) {
            writeFailedTestCaseToFile(baseFile.getAbsolutePath(),newFile.getAbsolutePath());
            throw new DiffException("Content mismatch in " + newFile.getAbsolutePath());
        }
    }
    
    public static void dumpAndCheckV1(Object obj, String fileName) throws Exception {
        ApiVersionConfig.call(ApiVersion.V1, () -> {
            dumpAndCheck(obj, fileName);
            return null;
        });
    }
    
    public static void dumpAndCheckV2(Object obj, String fileName) throws Exception {
        ApiVersionConfig.call(ApiVersion.V2, () -> {
            dumpAndCheck(obj, fileName);
            return null;
        });
    }
    
    public static void dumpAndCheck(Object obj, String fileName) throws Exception
    {
        try {
            dumpAndCheck(obj, fileName, false);
        }catch(DiffAssertException exp) {
            writeFailedTestCaseToFile(fileName);
            throw new RuntimeException("err file - " + fileName + "\n " + exp.getMessage(), exp);
        }
    }
    /**
     * Used to dump and compare the columns of json response.
     *
     * @param obj generally <code>Result</code> or <code>List<Result></code>.
     * Contains json content of response.
     * @param fileName name of json file to dump Without .json extension.
     * @throws Exception if content mis-match in base file and new file or if
     * base file is not present. Creates the base file as well.
     */
    public static void dumpAndCheck(Object obj, String fileName, boolean isErrorObject) throws Exception
    {
        String fileNameWithExtn = fileName + ".json";
        File baseFile = getBaseFile(fileNameWithExtn);
        File newFile = getNewFile(fileNameWithExtn);
        FileUtils.writeStringToFile(newFile, obj.toString());
        if(!baseFile.isFile()) { // basedata not present - dump it
            if(dumpBaseFiles){
                FileUtils.writeStringToFile(baseFile, obj.toString());
            }
            if(throwIfBaseFileNotExists){
                throw new RuntimeException("Base file not present " + baseFile.getAbsolutePath() + ". Hence writing");
            }
        } else {            
            if(isErrorObject) {
                if (!CBTestListener.matchTestResultWithBaseData(newFile, baseFile)) {
                    writeFailedTestCaseToFile(baseFile.getAbsolutePath(), newFile.getAbsolutePath());
                    throw new RuntimeException("DumpData and Basedata are not same.\n");
                }
                ;
            }else {
                DumpUtil.jsonReadNCompareObject(baseFile, newFile);
            }
        }
    }
    
    
    public static void dumpAndCheckContent(String newContent, String fileName) throws Exception {
        String fileNameWithExtn = fileName + ".txt";
        File baseFile = getBaseFile(fileNameWithExtn);
        File newFile = getNewFile(fileNameWithExtn);
        FileUtils.writeStringToFile(newFile, newContent);
        if(!baseFile.isFile()) { // basedata not present - dump it
            FileUtils.writeStringToFile(baseFile, newContent);
            if(throwIfBaseFileNotExists){
                throw new RuntimeException("Base file not present " + baseFile.getAbsolutePath() + ". Hence writing");
            }
        } else {
            String content = FileUtils.readFileToString(baseFile);   
            assert_(content.equals(newContent), "Base data and new data differs " + fileName );
        }
    }

    /**
     * Version specific directory for 'base' and 'new' files. For V1 - no version specific directory is maintained for 
     * backward compatibility with existing data.
     * @return 
     */
    public static String verSpecificDir(String dir) {
        switch(ApiVersionConfig.version()) {
            case V1:
                return dir;
            default:
                return dir + "/" + ApiVersionConfig.version().apiName;
        }
    }
    
    /**
     * initialize new data directory. Create directory if not present.
     * <br>
     * <code>e.g. chargebee-app/qa/../qa_data/basedata/dumpfiles/CouponTest/createSubscriptionWithCoupon</code>
     *
     * @return new data directory <code>File</code>object.
     */
    public static File getNewFilesDumpDir(){
        String newTestPath = "../qa_data/newdata" + "/dumpfiles/" + testCaseName;
        String newTestMethPath = verSpecificDir(newTestPath + "/" +testCaseMethodName);
        checkNCreateDirectories(new File(newTestPath), new File(newTestMethPath));
        return new File(newTestMethPath);
    }

    /**
     * initialize base data directory. Create directory if not present.
     * <br>
     * <code>e.g. chargebee-app/qa/../qa_data/newdata/dumpfiles/CouponTest/createSubscriptionWithCoupon</code>
     *
     * @return base data directory <code>File</code>object.
     */
    public static File getBaseFilesDumpDir(){
        String baseTestDirPath = "../qa_data/basedata" + "/dumpfiles/" + testCaseName;
        String baseTestMethPath = verSpecificDir(baseTestDirPath + "/" + testCaseMethodName);
        checkNCreateDirectories(new File(baseTestDirPath), new File(baseTestMethPath));
        return new File(baseTestMethPath);
    }
    
    /**
     *
     * @param fileNameWithExtn new data file name with extension
     * @return new file object for given file name
     * @throws Exception if file not found
     */
    public static File getNewFile(String fileNameWithExtn) throws Exception {
        return new File(getNewFilesDumpDir(),fileNameWithExtn);
    }
    
    /**
     *
     * @param fileNameWithExtn base data file name with extension
     * @return base file object for given file name
     * @throws Exception if file not found
     */
    public static File getBaseFile(String fileNameWithExtn) throws Exception {
        return new File(getBaseFilesDumpDir(),fileNameWithExtn);
    }
    
    /**
     * Used to dump specific content in a categories. Like for line items, you
     * can use grpName as line_items and testName can be item1.txt, item2.txt
     * etc.
     *
     * @param lines dumps list of <code>String</code>
     * @param grpName name of
     * @param testName file name in which content should be dumped under
     * <br> <code>qa/test-output/line_item/item1.txt</code>
     */
    public static void dumpList(Collection<String> lines, String grpName, String testName) {
        File file = getSuitableFile("test-output/dump/" + grpName + "/" + testName);
        try {
            FileUtils.writeLines(file, lines);
        } catch(IOException exp) {
            throw asRtExp(exp);
        }
    }
    
    /**
     * returns file if not exists. Otherwise add prefix/index and returns
     * <code>File</code> with new name. e.g. if example.txt is not present,
     * returns the same. If present, returns
     * <code>File</code> for example2.txt.
     *
     * @param path file path
     * @return <code>File </code> if not exists already. Otherwise add the
     * prefix/index(starts from 2) and creates and returns the same.
     */
    private static File getSuitableFile(String path) {
        File file = new File(path);
        int index = 2; // starting with 2
        while(true) {
            if(!file.exists()) {
                return file;
            }
            
            file = new File(path + "." + index);
            ++index;
        }
    }
    
    public static void main(String[] args) throws Exception {
        InitializerServlet.initStandAlone();
        new QATimeMachine().beforeDays(1);
        jsonReadNCompareObject(
                new File("../qa_data/basedata/dumpfiles/TcoCheckoutNewTest/offerCoupon/hosted_page.json"), 
                new File("../qa_data/newdata/dumpfiles/TcoCheckoutNewTest/offerCoupon/hosted_page.json"));
    }
    
    /**
     * Compares the json files under base data and new data. Track all mis-match
     * columns and throws exception if there are one or more mis-matches. It
     * will return all mis-matched columns in exception message.
     *
     * @param baseFile already dumped file for reference. Need to update if any
     * change in behavior.
     * @param dumpFile new json file with new data to compare with the reference
     * file.
     * @throws Exception if mis-match in base file and new file.
     */
    public static void jsonReadNCompareObject(File baseFile, File dumpFile) throws Exception
    {
        JSONObject jsonBaseObj = new JSONObject(FileUtils.readFileToString(baseFile));
        JSONObject jsonDumpObj = new JSONObject(FileUtils.readFileToString(dumpFile));
        int preLen = jsonBaseObj.toString().length();
        jsonReadNCompareObject(jsonBaseObj, jsonDumpObj);
        int postLen = jsonBaseObj.toString().length();
        if((boolean)CBPluginMgr.get(fixesIsModifiabe).apply() && (postLen != preLen || hasDiffUpdateBaseData )) { // base_file updated !!
//            FileUtils.copyFile(dumpFile, baseFile);
            FileUtils.writeStringToFile(baseFile.getAbsoluteFile(), jsonBaseObj.toString(2));
            log(">>>>>>>>>>>>>>>>>> Modified File" + baseFile.getCanonicalPath());
        }
    }
    
    public static void jsonReadNCompareObject(JSONObject jsonBaseObj, JSONObject jsonDumpObj) throws Exception {
        if (jsonBaseObj.has("object")) {
            compareObjects(jsonBaseObj, jsonDumpObj);
            return;
        }   
        Iterator<String> itr = jsonBaseObj.keys();        
        while(itr.hasNext())
        {
            String key = itr.next();
            assert_(jsonDumpObj.has(key),"There is "+ key + " expected,But it does not exist in the output.");
            if(jsonBaseObj.get(key) instanceof JSONArray)
            {
                JSONArray arr1 = (JSONArray) jsonBaseObj.get(key);
                JSONArray arr2 = (JSONArray) jsonDumpObj.get(key);                                                
                assert_((arr1.length()==arr2.length()), "BaseData and new data have different " + key);
                for (int i = 0; i < arr1.length(); i++)
                {
                    JSONObject baseObj = (JSONObject) arr1.get(i);
                    JSONObject dumpObj = (JSONObject) arr2.get(i);
                    jsonReadNCompareObject(baseObj, dumpObj);
                }
            }
            else if (jsonBaseObj.get(key) instanceof JSONObject) {
                JSONObject baseObj = (JSONObject) jsonBaseObj.get(key);
                JSONObject dumpObj = (JSONObject) jsonDumpObj.get(key);
                if (jsonBaseObj.has("object")) {
                    DumpUtil.compareObjects(baseObj, dumpObj);
                } else {
                    jsonReadNCompareObject(baseObj, dumpObj);
                }
            } else {           
                Object baseVal = jsonBaseObj.get(key);
                Object newVal = jsonDumpObj.get(key);
                if("next_offset".equals(key)) {
                    assert_(newVal != null, "next_offset not present in newData");
                } else {
                    assert_(baseVal.equals(newVal), "BaseData and new data have different " + key);
                }
            }
            jsonDumpObj.remove(key);
        }
        assert_((jsonDumpObj.length()==0), "New file has keys that does not exist in baseData [" + jsonDumpObj.keys() + "]");            
    }

    /**
     * compares two json objects row by row in using difftool
     *
     * @see DiffTool
     * @param baseObj old json content
     * @param dumpObj new json content
     * @throws Exception if one or more mis-match in columns.
     */
    public static void compareObjects(JSONObject baseObj, JSONObject dumpObj) throws Exception {
        DiffTool tool = _compareObjects(baseObj, dumpObj);
        assert_(tool.errors().isEmpty(), tool.toString());
        if (CBPluginMgr.isNotNull(assertForLineDiscountsExtension)) {
            CBPluginMgr.get(assertForLineDiscountsExtension).accept(dumpObj);
        }
        Fixes.testBaseDataFix(baseObj);
    }
    
    public static DiffTool _compareObjects(JSONObject baseObj, JSONObject dumpObj) throws Exception {        
        CBPluginMgr.get(fixesModifyBaseObjectIfNeeded).apply(baseObj, dumpObj);
        Class clazz1;        
        try {
            clazz1 = ClazzUtil.getClaz((String) CBPluginMgr.get(apiCodeGenExtension).apply(true), baseObj.getString("object"));
        } catch (Exception ex) {
            DiffTool tool =(DiffTool)CBPluginMgr.get(copyDataHelperCheck).apply(baseObj,dumpObj);
            if (tool != null) {
                return tool;
            }
            throw ex;
        }
        Object baseClass = ClazzUtil.createInstance(clazz1, baseObj);
                    
        Class clazz2 = ClazzUtil.getClaz((String) CBPluginMgr.get(apiCodeGenExtension).apply(true), dumpObj.getString("object"));
        Object dumpClass = ClazzUtil.createInstance(clazz2, dumpObj);
        // DiffTool will be present as it is. 
        DiffTool tool = DiffTool.findDiff(baseClass, dumpClass);
        diffCustomFields(tool, baseClass, dumpClass);
        return tool;
    }
    
    
    private static void diffCustomFields(DiffTool tool, Object baseClass, Object dumpClass ){
        switch(ApiVersionConfig.version()) {
            case V1:
                 tool.diffCustomFields(((Resource)baseClass).jsonObj, ((Resource)dumpClass).jsonObj);
                 tool.diffCustomFields(((Resource)dumpClass).jsonObj, ((Resource)baseClass).jsonObj);
                break;
            case V2:
                 tool.diffCustomFields(((com.chargebee.v2.internal.Resource)baseClass).jsonObj, 
                         ((com.chargebee.v2.internal.Resource)dumpClass).jsonObj);
                 tool.diffCustomFields(((com.chargebee.v2.internal.Resource)dumpClass).jsonObj, 
                         ((com.chargebee.v2.internal.Resource)baseClass).jsonObj);
                break;
            default:
                throw new RuntimeException("Unhandled Version " + ApiVersionConfig.version());
        }
        
    }
    
    /**
     * Creates directory if not present already with test file name. Creates
     * directory of not present already with test case method name under test
     * file name direstory
     *
     * @param testDir test file directory to store base/new data.
     * @param testMethodDir test case method directory to store base/new data.
     */
    private static void checkNCreateDirectories(File testDir, File testMethodDir)
    {
        if(!testDir.exists()) {
            testDir.mkdir();
        }
        
        if(!testMethodDir.exists()) {
            testMethodDir.mkdir();
        }
    }

    public static class DiffException extends Exception{
       
        /**
         *
         * @param msg error message for mis-match fields with old and new
         * values.
         */
        public DiffException(String msg){
            super(msg);
        }
        
    }
    
    /**
     * Throws exception if condition is not true with given error message.
     *
     * @param check if <code>false</code>, throws exception.
     * @param msg exception message.
     */
    public static void assert_(boolean check,String msg){
        if(!check){
            throw new DiffAssertException(msg);
        }
    }
    public static void genAndCheckCSVFile(String fileName, String csvClass) throws Exception {
        generateCSVFile(csvClass);
        File baseDataDir = DumpUtil.getBaseFilesDumpDir();
        File destDir = DumpUtil.getNewFilesDumpDir();
        cleanDirIfExists(destDir);//clearing download and newdata directories
        if (baseDataDir.list().length == 0) {//if basedatadir does not contain the csv file
            FileUtils.copyFile(SeleniumBase.dloadDir.listFiles()[0], new File(baseDataDir.getAbsolutePath() + "/" + fileName));//copying the downloaded csv file to basedata dir
            throw new RuntimeException("Base file not present " + baseDataDir.listFiles()[0].getAbsolutePath() + ". Hence writing");
        }
        FileUtils.moveFile(SeleniumBase.dloadDir.listFiles()[0], new File(destDir.getAbsolutePath() + "/" + fileName));//moving the csv file from to downloads to newdata dir
        DemoDataFileDiff.diffData(baseDataDir.getAbsolutePath(),
                destDir.getAbsolutePath(), "csv", true);
    }

    private static void generateCSVFile(String csvClass) throws Exception {//method to generate csv file
        cleanDirIfExists(SeleniumBase.dloadDir);//clearing download and newdata directories
        String url = (String) DriverUtil.executeScript("return $(\"." + csvClass + "\").click().attr(\"href\");");
        DriverUtil.get(UrlHelper.url(url));
        WaitUtil.waitUntil(new NonEmptyDirPredicate(SeleniumBase.dloadDir));
    }

    /**
     * Cleans the directory if exists.
     * @param dir directory which needs to be cleaned
     * @throws Exception if directory not found.
     */
    public static void cleanDirIfExists(File dir) throws Exception {//method to clear the dir if exists
        FileUtils.deleteDirectory(dir);
        dir.mkdir();
    }
    
    /**
     * Creates the CSV file in the baseData and newData directory.
     * @param fileName - <fileNameWithExtension>
     * @param content - <fileContent>
     * @throws Exception 
     */
    public static void dumpCSVFile(String fileName, String content) throws Exception {
        File baseFile = DumpUtil.getBaseFile(fileName);
        File newFile = DumpUtil.getNewFile(fileName);
        FileUtils.writeStringToFile(newFile, content); // creating new file
        if (!baseFile.exists()) { // basedata not present - dump it
            FileUtils.writeStringToFile(baseFile, content);
            throw new RuntimeException("Base file is not present " + baseFile.getAbsolutePath() + ". Hence writing");
        }
    }
    
    /**
     * Using demoDataFileDiff to find diff between base and newDataDir.
     * Note: Generate the csv files in the baseDir and newDir using above method
     * dumpCSVFile(String fileName, String content)
     * @throws Exception when baseDataDir or newDataDir is empty.
     */
    public static void diffCSVFiles() throws Exception {
        File baseDataDir = DumpUtil.getBaseFilesDumpDir();
        File destDir = DumpUtil.getNewFilesDumpDir();
        if (baseDataDir.list().length == 0) {//if basedatadir does not contain the csv file
            throw new RuntimeException("Base directory " + baseDataDir.listFiles()[0].getAbsolutePath() + " is empty.");
        }
        if (destDir.list().length == 0) {//if destDir does not contain the csv file
            throw new RuntimeException("Destination directory " + destDir.listFiles()[0].getAbsolutePath() + " is empty.");
        }
        DemoDataFileDiff.diffData(baseDataDir.getAbsolutePath(), destDir.getAbsolutePath(), "csv", true);
    }
    
    /**
     * If there is a Diff Exception, DiffAssertException, 
     * write the class name , method name and failed file name to a file
     * @param srcPath
     * @param desPath
     * @throws java.io.IOException 
     */
    public static void writeFailedTestCaseToFile(String srcPath, String desPath) throws IOException {
        String output = getClassNameForDumpData() + ":" + DumpUtil.testCaseMethodName + ":" + srcPath + ":" + desPath +"\n";        
        FileWriter fw = new FileWriter(dumpDataFile, true);
        try{
            fw.write(output);
        }finally{
            fw.close();
        }
    }
    
    public static void writeFailedTestCaseToFile(String fileName) throws Exception{
        String fileNameWithExtn = fileName + ".json";
        File baseFile = getBaseFile(fileNameWithExtn);
        File newFile = getNewFile(fileNameWithExtn);
        writeFailedTestCaseToFile(baseFile.getAbsolutePath(), newFile.getAbsolutePath());
    }
    /**
     * Gives the correct class name to append in inputs
     * @return 
     */
    public static String getClassNameForDumpData(){
        String className = DumpUtil.testCaseName;
        if (!Strings.isNullOrEmpty(javaClientVersion)) {
            className  = className + "-"+javaClientVersion;
        }
        return className;
    }
    
    public static void storeApiVersion(String version){
        //Write javaclient version to a file 
        javaClientVersion = version;
        File javaClientVersionFile = new File(System.getProperty("user.home")+"/work/chargebee-app/qa/test-output/javaclient.txt");
        try {
            FileUtils.writeStringToFile(javaClientVersionFile, "-"+javaClientVersion);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to write javaclient version to file", ex);
        }
    }
}

