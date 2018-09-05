/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.net.*;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Pradyumna Khawas
 */
public class JavaApplication3 {

    public static void main(String[] args) throws UnknownHostException, MalformedURLException, IOException
    { 
        System.out.print("Enter the thing you want info for:");
        Scanner scan = new Scanner(System.in);
        String tValue = scan.nextLine();
        tValue = manipString(tValue);
        String data = smallFromBig(tValue);
        data = removeLinks(data);
        data = extractURL(data,data);
        convertToHtml(data);
        openTheFile("info.html");
    }
    public static String manipString(String s)
    {
        String[] temp = s.split(" ");
        for(int i=0;i<temp.length;i++)
        {
            String x = temp[i];
            x = x.substring(0, 1).toUpperCase() + x.substring(1);
            temp[i] = x;
        }
        s = String.join("_", temp);
        return s;
    }
    public static String smallFromBig(String tValue) throws UnknownHostException, MalformedURLException, IOException
    {
        InetAddress Address = InetAddress.getLocalHost(); 
        System.out.println(Address);  
        URL oracle = new URL("https://en.wikipedia.org/wiki/"+tValue);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine="";
        String outputLine="<html><head><title>Info that you want</title></head><body><";
        int fval = -1, lval = -1;
        int ans=0, i = 0;
        while ((inputLine = in.readLine()) != null)
        {            
            if(ans==0)
            {
                fval = inputLine.indexOf("table class=\"infobox");
                if(fval!=-1)
                    ans=ans+1;
            }
            if(ans==1)
            {
                inputLine = inputLine.substring(fval);
                ans=ans+1;
            }
            if(ans==2)
            {
                lval = inputLine.indexOf("</table>");
                if(lval!=-1)
                {
                    inputLine = inputLine.substring(0, lval);
                    outputLine = outputLine.concat(inputLine);
                    ans=ans+1;
                    break;
                }
                else
                {
                    outputLine = outputLine.concat(inputLine);
                }
            }
            i++;
        }
        outputLine = outputLine.concat("</table></body></html>");
        in.close();
        return outputLine;
    }
    public static void convertToHtml(String s) throws IOException
    {
        BufferedWriter bw = null;
        FileWriter fw = null;
        File file = new File("info.html");
        fw = new FileWriter(file.getAbsoluteFile());
	bw = new BufferedWriter(fw);
        bw.write(s);
        bw.close();
        fw.close();
    }
    public static void openTheFile(String url) throws IOException
    {
        Runtime rTime = Runtime.getRuntime();
        String command = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe " + url; 
        rTime.exec(command); 
    }
    public static String removeLinks(String s)
    {
        int fval=-1,lval=-1;
        String temp;
        while(s.indexOf("<a ")!=-1)
        {
            fval = s.indexOf("<a ");
            temp = s.substring(0, fval);
            s = s.substring(fval);
            lval = s.indexOf(">");
            s = s.substring(lval+1);
            temp = temp.concat(s);
            s = temp;
            fval = s.indexOf("</a>");
            temp = s.substring(0,fval);
            s = s.substring(fval+4);
            s = temp.concat(s);
        }
        return s;
    }
    public static String extractURL(String s,String data) throws MalformedURLException, IOException
    {
        String x="",temp;
        int fval,lval;
        int count = 0,t=0;
        while(s.indexOf("src=",t+1)!=-1)
        {
            count++;
            fval = s.indexOf("src=",t+1);
            temp = s.substring(fval+5);
            lval = temp.indexOf("\"");
            x = temp.substring(0, lval);
            downloadImage(x,count);
            data = replaceURL(data,"image"+Integer.toString(count)+".jpg",t);
            t = fval;
            
        }
        return data;
    }
    public static void downloadImage(String turl,int x) throws MalformedURLException, IOException
    {
        turl = "https:".concat(turl);
        URL url = new URL(turl);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
           out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        FileOutputStream fos = new FileOutputStream("image"+Integer.toString(x)+".jpg");
        fos.write(response);
        fos.close();
    }
    public static String replaceURL(String s,String url,int t)
    {
        String x="",temp;
        int fval,lval;
        fval = s.indexOf("src=",t+1);
        temp = s.substring(0,fval+5);
        s = s.substring(fval+5);
        temp = temp.concat(url);
        lval = s.indexOf("\"");
        x = s.substring(lval);
        temp = temp.concat(x);
        s= temp;
        
        fval = s.indexOf("srcset=\"",t+1);
        temp = s.substring(0,fval);
        s = s.substring(fval+8);
        lval = s.indexOf("\"");
        x = s.substring(lval+1);
        temp = temp.concat(x);
        return temp;
    }
}
