import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.PasswordAuthentication;



public class Main {
    public static void main(String[] args) {

        ElitUye elitUye = new ElitUye();
        GenelUye genelUye = new GenelUye();
        Mail mail = new Mail();

        Scanner scanner = new Scanner(System.in);

        System.out.println("1- Elit üye ekleme\n2- Genel Üye ekleme\n3- Mail Gönderme");
        String tercih = scanner.nextLine();

        switch (tercih){
            case "1":
                elitUye.uyeGiris();
                break;
            case "2":
                genelUye.uyeGiris();
                break;
            case "3":
                System.out.println("1- Elit üyelere mail\n2- Genel üyelere mail\n3- Tüm üyelere mail");
                tercih = scanner.nextLine();

                switch (tercih){
                    case "1":
                        elitUye.getMail();
                        break;
                    case "2":
                        genelUye.getMail();
                        break;
                    case "3":
                        elitUye.getMail();
                        genelUye.getMail();
                }
        }
    }
}

class Uye {
    String ad;
    String soyad;
    String eposta;
    String dosyaYolu = "C:\\Users\\ay\\Desktop\\kullanicilar.txt";

    public void uyeKaydet(int islem_turu){
        String eklenecek_satir = ad + '\t' + soyad + '\t' + eposta;
        String[] satirlar = dosyadanArraye(dosyaYolu);
        switch (islem_turu){
            case 1:
                satirlar = arrayeVeriEkle(satirlar, eklenecek_satir, 1);
                break;
            case 2:
                satirlar = arrayeVeriEkle(satirlar, eklenecek_satir, satirlar.length);
                break;
        }

        arraydenDosyaya(dosyaYolu, satirlar);

    }
    public void arraydenDosyaya(String _dosyaYolu, String[] _satirlar){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(_dosyaYolu));

            for (String satir: _satirlar) {

                bufferedWriter.append(satir + '\n');
            }
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] arrayeVeriEkle(String[] array, String veri, int index){
        String[] new_arr = new String[array.length+1];

        for(int i = array.length - 1; i>=index; i--){
            new_arr[i+1] = array[i];
        }

        for(int i = 0; i<index;i++){
            new_arr[i] = array[i];
        }
        new_arr[index] = veri;
        return new_arr;
    }

    public String[] dosyadanArraye(String _filePath){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(_filePath));

            List<String> stringFile = new ArrayList<String>();
            String line = bufferedReader.readLine();
            while(line != null) {
                stringFile.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String[] lines = stringFile.toArray(new String[0]);
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class ElitUye extends Uye {
    public void uyeGiris(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Elit uye adı : ");
        ad = scanner.nextLine();
        System.out.println("Elit uye soyadı : ");
        soyad = scanner.nextLine();
        System.out.println("Elit uye eposta adresi : ");
        eposta = scanner.nextLine();
        System.out.println("Elit Uye Kaydediliyor.");
        uyeKaydet(1);
        System.out.println("Elit Uye Kaydedildi.");

        scanner.close();
    }

    public String[] getMail(){
        String[] lines = dosyadanArraye(dosyaYolu);
        int elitUyeSayisi = 0;
        for (int i = 1; !lines[i].equals(""); i++) {
            elitUyeSayisi++;
        }
        String[] mails = new String[elitUyeSayisi];
        for (int i = 1; !lines[i].equals(""); i++) {
            String[] elitUserData = lines[i].split("\t");
            mails[i - 1] = elitUserData[elitUserData.length - 1];
        }
        System.out.println(mails.length);
        return mails;
    }
}

class GenelUye extends Uye {
    public void uyeGiris(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Genel uye adı : ");
        ad = scanner.nextLine();
        System.out.println("Genel uye soyadı : ");
        soyad = scanner.nextLine();
        System.out.println("Genel uye eposta adresi : ");
        eposta = scanner.nextLine();
        System.out.println("Genel Uye Kaydediliyor.");
        uyeKaydet(2);
        System.out.println("Genel Uye Kaydedildi.");

        scanner.close();
    }

    public String[] getMail(){
        String[] lines = dosyadanArraye(dosyaYolu);
        int genelUyeSayisi = 0;
        for (int i = 1; !lines[i].equals(""); i++) {
            genelUyeSayisi++;
        }
        String[] mails = new String[genelUyeSayisi];
        for (int i = 1; !lines[i].equals(""); i++) {
            String[] genelUserData = lines[i].split("\t");
            mails[i - 1] = genelUserData[genelUserData.length - 1];
        }
        System.out.println(mails.length);
        return mails;
    }
}

class Mail{
    public void mailHazırla(){

    }

    public void mailGonder(){
        
    }
}