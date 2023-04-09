import java.io.*;
import javax.mail.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.internet.*;

public class Main {
    public static void main(String[] args) {

        UyeIslemleri uyeIslemleri = new UyeIslemleri();
        MailIslemleri mailIslemleri = new MailIslemleri();

        Scanner scanner = new Scanner(System.in);

        boolean devam = true; // Kullanıcı aksini istemedikçe menülerin açık kalması için değişken.

        while (devam) {
            System.out.println("\nNe yapmak istersiniz?");
            System.out.println("1- Elit Üye kaydı");
            System.out.println("2- Genel Üye Kaydı");
            System.out.println("3- Üyelere Mail Gönder");
            System.out.println("4- Çıkış");
            System.out.print("Seçiminiz (1-4): ");
            String secim = scanner.next(); // Seçime trim uygulanarak işlem sırasında değişkenin okunmasında yazmasında sıkıntı çıkmasının önüne geçilir

            if (secim.trim().equals("1")) {
                uyeIslemleri.uyeGiris("Elit");
            } else if (secim.trim().equals("2")) {
                uyeIslemleri.uyeGiris("Genel");
            } else if (secim.trim().equals("3")) {
                boolean mailDevam=true;
                while (mailDevam) {
                    System.out.println("\nKime mail göndermek istersiniz?");
                    System.out.println("1- Elit Üyelere Mail Gönder");
                    System.out.println("2- Genel Üyelere Mail Gönder");
                    System.out.println("3- Tüm Üyelere Mail Gönder");
                    System.out.println("4- Geri");
                    System.out.print("Seçiminiz (1-4): ");
                    String mailSecim = scanner.next();
                    if (mailSecim.trim().equals("1")) {
                        mailIslemleri.mailOlustur();
                        mailIslemleri.elitUyelereMailGonder();
                        break;
                    } else if (mailSecim.trim().equals("2")) {
                        mailIslemleri.mailOlustur();
                        mailIslemleri.genelUyelereMailGonder();
                        break;
                    } else if (mailSecim.trim().equals("3")) {
                        mailIslemleri.mailOlustur();
                        mailIslemleri.tumUyelereMailGonder();
                    }else if (mailSecim.trim().equals("4")) {
                        mailDevam=false;
                    } else {
                        System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
                    }
                }
            }else if (secim.trim().equals("4")) {
                devam=false;
            }
            else {
                System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
            }
        }

    }
}


// Bu class uye bilgilerini tutan classtır.
class Uye {
    String ad;
    String soyad;
    String eposta;

    public void setAd(String ad) { this.ad = ad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public void setEposta(String eposta) { this.eposta = eposta; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getEposta() { return eposta; }
}


// Bu class ile uye girişi, kayiti, ve eklenmesi işlemleri yapılır.
class UyeIslemleri{

    DosyaIslemleri dosyaIslemleri = new DosyaIslemleri();

    // Bu fonksiyon kullanıcıdan üye bilgilerini alarak onları oluşturulan uye classıyla düzenler ve kaydedileceği fonksiyona uye olarak verir.
    public void uyeGiris(String tip){
        Scanner scanner = new Scanner(System.in);
        Uye uye = new Uye();
        System.out.println(tip + " uye adı : ");
        uye.setAd(scanner.nextLine());
        System.out.println(tip + " uye soyadı : ");
        uye.setSoyad(scanner.nextLine());
        System.out.println(tip + " uye eposta adresi : ");
        uye.setEposta(scanner.nextLine());
        System.out.println(tip + " Uye Kaydediliyor.");
        uyeKayit(uye, tip);
        System.out.println(tip + " Uye Kaydedildi.");
    }

    //Bu fonksiyon hazırda var olan üye dosyasının uygun noktasına yeni uyeyi konumlandırır ve dosyayı kaydeder.
    public void uyeKayit(Uye uye, String tip){
        String yeniUyeStr = uye.getAd() + '\t' + uye.getSoyad() + '\t' + uye.getEposta();
        String[] satirlar = dosyaIslemleri.dosyadanUyeListesiniOku();

        switch (tip) {
            case "Elit":
                satirlar = uyeListesineVeriEkle(satirlar, yeniUyeStr, 1);
                break;
            case "Genel":
                satirlar = uyeListesineVeriEkle(satirlar, yeniUyeStr, satirlar.length);
                break;
        }

        dosyaIslemleri.uyeListesiniDosyayaYaz(satirlar);
    }

    // Bu fonksiyon dosyadan okunan uye listesinin istenilen indeksine yeni uyeyi koymaya yarar.
    public String[] uyeListesineVeriEkle(String[] array, String veri, int index) {
        String[] new_arr = new String[array.length + 1];

        for (int i = array.length - 1; i >= index; i--) {
            new_arr[i + 1] = array[i];
        }

        for (int i = 0; i < index; i++) {
            new_arr[i] = array[i];
        }
        new_arr[index] = veri;
        return new_arr;
    }

}

//Bu class ile dosyadan listeye aktarım, listeden dosyaya aktarım, belirli kullanıcı tipinin uye nesnesi olarak okunması ve bilgilerine erişimi sağlanır.
class DosyaIslemleri {

    private String dosyaYolu = "Kullanicilar.txt"; // Dosya Yolu istenilirse değiştirilebilinir.
    public String getDosyaYolu() { return dosyaYolu; }
    public void setDosyaYolu(String dosyaYolu) { this.dosyaYolu = dosyaYolu; }

    // Classın çağırılmasıyla dosyaya bağlanır.
    public DosyaIslemleri(){
        File f = new File(dosyaYolu);
        dosyaYolu = f.getAbsolutePath();
    }

    //İşlem sürecinde oluşturulan üye listesini dosyaya yazar.
    public void uyeListesiniDosyayaYaz(String[] _satirlar){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dosyaYolu));
            for (String satir: _satirlar) {
                bufferedWriter.append(satir + '\n');
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Dosyada bulunan üyeleri listeye kaydeder.
    public String[] dosyadanUyeListesiniOku(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dosyaYolu));
            List<String> dosya = new ArrayList<String>();
            String satir = bufferedReader.readLine();
            while(satir != null) {
                dosya.add(satir);
                satir = bufferedReader.readLine();
            }
            bufferedReader.close();
            return dosya.toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Elit üyeleri dosyadan okur.
    public List<Uye> elitUyeleriAl(){
        List<Uye> uyeler = new ArrayList<>();
        Uye uye;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dosyaYolu));
            String satir = bufferedReader.readLine();
            while (satir != null) { // satır null olana kadar dönmeye devam eder.
                if (!satir.trim().contains("Genel Uyeler")) { // Genel Uyeler ifadesine gelmedikçe dönmeye devam eder
                    if (!satir.isEmpty() && !satir.startsWith("#")) { // Satir boş ya da # işareti ile başlamıyorken dönmeye devam eder.
                        uye = new Uye();
                        // Satirdan uye verilerini uyeye kaydeder.
                        uye.setAd(satir.split("\t")[0]);
                        uye.setSoyad(satir.split("\t")[1]);
                        uye.setEposta(satir.split("\t")[2]);
                        uyeler.add(uye);
                    }
                    satir = bufferedReader.readLine();
                } else {
                    satir = null;
                }

            }
            bufferedReader.close();
            return uyeler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Genel uyeleri dosyadan okur.
    public List<Uye> genelUyeleriAl() {
        List<Uye> uyeler=new ArrayList<>();
        Uye uye;
        boolean isGenel = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dosyaYolu));

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.contains("#Genel")) {
                    isGenel = true;
                    line = bufferedReader.readLine();
                }
                if (isGenel) {
                    if (line.isEmpty() || line.trim().length() == 0) {
                        line = null;
                    } else {
                        uye=new Uye();
                        uye.setAd(line.split("\t")[0]);
                        uye.setSoyad(line.split("\t")[1]);
                        uye.setEposta(line.split("\t")[2]);
                        uyeler.add(uye);
                        line = bufferedReader.readLine();
                    }
                } else {
                    line = bufferedReader.readLine();
                }
            }
            bufferedReader.close();
            return uyeler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

// Bu class ile Mail ayarlamaları yapılır.
class Mail{

    String username = "<Emailinizi Buraya Yazın>"; //Kullanıcının kendi mailini gireceiği yer.
    String password = "<Şifrenizi Buraya Yazın>"; //Kullanıcının mail şifresini gireceği yer.
    Properties props;

    public Mail(){
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    // Bu fonksiyon mail gondermeye yarar.
    public void mailGonder(String aliciMail, String konu, String mesaj) {

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(aliciMail));
            message.setSubject(konu);
            message.setText(mesaj);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

// Mail konusu, gövdesi, ve hangi kullanıcılara gönderileceği işlemleri bu classta yapılır.
class MailIslemleri extends Mail {

    DosyaIslemleri dosyaIslemleri = new DosyaIslemleri();
    Scanner scanner = new Scanner(System.in);

    String konu;
    String mesaj;

    public void mailOlustur() {
        System.out.println("Konu giriniz : ");
        konu = scanner.nextLine();
        System.out.println("Mesaj giriniz : ");
        mesaj = scanner.nextLine();
    }

    public void elitUyelereMailGonder() {
        List<Uye> uyeler = dosyaIslemleri.elitUyeleriAl();
        for (Uye u : uyeler) {
            System.out.println(u.getAd() + " " + u.getSoyad() + " " + u.getEposta() + " bilgilerine sahip elit uyeye mail gonderiliyor.");
            mailGonder(u.getEposta(), konu, mesaj);
        }
        System.out.println(uyeler.size() + " tane elit uyeye mail gonderildi.");
    }

    public void genelUyelereMailGonder() {
        List<Uye> uyeler = dosyaIslemleri.genelUyeleriAl();
        for (Uye u : uyeler) {
            System.out.println(u.getAd() + " " + u.getSoyad() + " " + u.getEposta() + " bilgilerine sahip genel uyeye mail gonderiliyor.");
            mailGonder(u.getEposta(), konu, mesaj);
        }
        System.out.println(uyeler.size() + " tane genel uyeye mail gonderildi.");
    }

    public void tumUyelereMailGonder() {
        elitUyelereMailGonder();
        genelUyelereMailGonder();
        System.out.println("Tüm uyelere nail gonderildi");
    }

}