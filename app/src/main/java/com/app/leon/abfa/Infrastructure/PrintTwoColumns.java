package com.app.leon.abfa.Infrastructure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.app.leon.abfa.Models.ViewModels.PrintableObject;
import com.bxl.BXLConst;
import com.bxl.config.editor.BXLConfigLoader;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;

/**
 * Created by Leon on 1/16/2018.
 */

public final class PrintTwoColumns {
    final int length = 42;
    Context context;
    StringBuffer stringBuffer;
    private POSPrinter posPrinter;

    public PrintTwoColumns(POSPrinter posPrinter, Context context) {
        this.context = context;
        this.posPrinter = posPrinter;
    }

    public void printReceiptForFarsi(ArrayList<PrintableObject> printableObjects1, String address,
                                     String logicalName, ArrayList<PrintableObject> printableObjects2) {
        BitmapDrawable bitmapDrawable = null;
        Bitmap bitmap = null;
        try {
            posPrinter.open(logicalName);
            posPrinter.claim(0);
            posPrinter.setDeviceEnabled(true);
            posPrinter.setCharacterEncoding(BXLConst.CS_FARSI);//CE_UTF8
            posPrinter.setCharacterSet(BXLConst.CS_FARSI);
            posPrinter.setOptReorderForFarsi(BXLConst.OPT_REORDER_FARSI_MIXED);

            printFixed(address);
            String seperator = "-";
            printSeperator(seperator);

            printRows(printableObjects1);

            printSeperator(seperator);

            printRows(printableObjects2);
            printSeperator(seperator);


            String[] data = {"\n" + "متن ثابت 1", "\n" + "متن ثابت 2"};
            printFixed(data);

            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n\n\n\n\n" + EscapeSequence.Normal);
            posPrinter.cutPaper(90);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                posPrinter.close();
            } catch (JposException e) {
                e.printStackTrace();
            }
        }

    }

    private void printFixed(String... fixedTexts) {
        for (String fixedText : fixedTexts) {
            printText(fixedText);
        }
    }

    private void printRows(ArrayList<PrintableObject> printableObjects) {
        for (PrintableObject printableObject : printableObjects) {
            printRow(printableObject);
        }
    }

    private void printRow(PrintableObject printableObject) {
        String key = printableObject.getKey(),
                value = printableObject.getValue();
        StringBuffer stringBuffer = new StringBuffer(key);
        for (int i = stringBuffer.length(); i < length / 2; i++)
            stringBuffer.append(" ");
        for (int i = length / 2; i < length - value.length(); i++)
            stringBuffer.append(" ");
        stringBuffer.append(value);
        printText(stringBuffer);
    }

    private void printBitmap(int resourceId) {
        BitmapDrawable bitmapDrawable = null;
        Bitmap bitmap = null;
        try {
            bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(resourceId);
            bitmap = bitmapDrawable.getBitmap();
            ByteBuffer bitmapBuffer = ByteBuffer.allocate(4);
            bitmapBuffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
            bitmapBuffer.put((byte) 80);
            bitmapBuffer.put((byte) 0x00);
            bitmapBuffer.put((byte) 0x00);
            try {
                posPrinter.printBitmap(bitmapBuffer.getInt(0), bitmap, posPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_CENTER);
            } catch (JposException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap = null;
            bitmapDrawable = null;
        }
    }

    private void printSeperator(CharSequence c) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
            stringBuilder = stringBuilder.append(c);
        printText(stringBuilder);
    }

    private void printText(CharSequence sequence) {
        try {
            posPrinter.printNormal(POSPrinterConst.PTR_BCS_Code93, "\n" + replaceChar(sequence) + EscapeSequence.Center
                    + EscapeSequence.Scale_1_time_horizontally + EscapeSequence.Scale_1_time_vertically);
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    private CharSequence replaceChar(CharSequence sequence) {
        String s = sequence.toString();
        s.replaceAll("ا", "ا");
        return s;

    }

    public String getSUEMessage(int status) {
        switch (status) {
            case JposConst.JPOS_SUE_POWER_ONLINE:
                return "Power on";

            case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
                return "Power off";

            case POSPrinterConst.PTR_SUE_COVER_OPEN:
                return "Cover Open";

            case POSPrinterConst.PTR_SUE_COVER_OK:
                return "Cover OK";

            case POSPrinterConst.PTR_SUE_REC_EMPTY:
                return "Receipt Paper Empty";

            case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
                return "Receipt Paper Near Empty";

            case POSPrinterConst.PTR_SUE_REC_PAPEROK:
                return "Receipt Paper OK";

            case POSPrinterConst.PTR_SUE_IDLE:
                return "Printer Idle";

            default:
                return "Unknown";
        }
    }

    public String getERMessage(int status) {
        switch (status) {
            case POSPrinterConst.JPOS_EPTR_COVER_OPEN:
                return "Cover open";
            case POSPrinterConst.JPOS_EPTR_REC_EMPTY:
                return "Paper empty";
            case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
                return "Power off";
            default:
                return "Unknown";
        }
    }

    public String setProductName(String name) {
        String productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200II;
        if ((name.indexOf("SPP-R200II") >= 0)) {
            if (name.length() > 10) {
                if (name.substring(10, 11).equals("I")) {
                    productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200III;
                }
            }
        } else if ((name.indexOf("SPP-R210") >= 0)) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R210;
        } else if ((name.indexOf("SPP-R310") >= 0)) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R310;
        } else if ((name.indexOf("SPP-R300") >= 0)) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R300;
        } else if ((name.indexOf("SPP-R400") >= 0)) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R400;
        }
        return productName;
    }
}
