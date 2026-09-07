package com.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@SpringBootApplication
@RestController
public class GalleryApplication {

    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        SpringApplication.run(GalleryApplication.class, args);
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("login") == null) {
            return loginPage();
        }
        return galleryPage();
    }

    @GetMapping("/login")
    public String loginPage() {
        return loginPageHtml(null);
    }

    @GetMapping("/check")
    public String check(@RequestParam String pwd, HttpSession session) {
        if (PASSWORD.equals(pwd)) {
            session.setAttribute("login", true);
            return galleryPage();
        }
        return loginPageHtml("❌ 密码错误");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return loginPageHtml(null);
    }

    private String loginPageHtml(String error) {
        String err = error != null ? "<div style='color:#f87171;margin-top:14px'>" + error + "</div>" : "";
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>密码验证</title>" +
               "<style>body{background:#0f172a;color:#fff;font-family:system-ui;display:flex;justify-content:center;align-items:center;min-height:100vh;margin:0}" +
               ".box{background:rgba(255,255,255,0.05);border-radius:30px;padding:48px 36px;width:350px;text-align:center}" +
               ".box h2{font-size:24px;font-weight:400;margin-bottom:4px}" +
               ".box p{color:#94a3b8;font-size:14px;margin-bottom:28px}" +
               ".box input{width:100%;padding:16px;border-radius:60px;border:1px solid rgba(255,255,255,0.1);background:#1e293b;color:#fff;font-size:18px;text-align:center;outline:none}" +
               ".box input:focus{border-color:#818cf8}" +
               ".box button{width:100%;padding:16px;margin-top:14px;background:#818cf8;border:none;border-radius:60px;color:#fff;font-size:18px;font-weight:600;cursor:pointer}" +
               ".box button:hover{transform:scale(1.02)}" +
               ".hint{color:#475569;font-size:12px;margin-top:16px}" +
               "</style></head><body><div class='box'>" +
               "<h2>🔐 请输入密码</h2><p>验证后进入图片展示</p>" +
               "<form method='get' action='/check'>" +
               "<input type='password' name='pwd' placeholder='请输入密码' autofocus>" +
               "<button type='submit'>进入</button>" +
               "</form>" + err +
               "<div class='hint'>💡 默认密码：1234</div>" +
               "</div></body></html>";
    }

    // ============================================================
    //  📸 图片展示页面
    //  改这里：把 photo1.jpg 改成您的图片文件名
    // ============================================================
    private String galleryPage() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>图片展示</title>" +
               "<style>" +
               "body{background:#0f172a;color:#fff;font-family:system-ui;padding:30px;text-align:center;margin:0}" +
               "h1{font-size:28px;font-weight:500;margin-bottom:20px}" +
               ".gallery{display:grid;grid-template-columns:repeat(auto-fill,minmax(250px,1fr));gap:20px;max-width:1000px;margin:0 auto}" +
               ".card{background:rgba(255,255,255,0.05);border-radius:16px;overflow:hidden;border:1px solid rgba(255,255,255,0.06)}" +
               ".card img{width:100%;aspect-ratio:1/1;object-fit:cover;display:block}" +
               ".logout{display:inline-block;margin-top:30px;padding:10px 30px;background:#ef4444;color:#fff;border-radius:30px;text-decoration:none}" +
               "</style></head><body>" +
               "<h1>📸 图片展示</h1>" +
               "<div class='gallery'>" +
               // ═══════════════════════════════════════════════════════
               //  👇 改这里：把 photo1.jpg 改成您的图片文件名
               //  要加图片就复制一行，改文件名
               // ═══════════════════════════════════════════════════════
              "<div class='card'><img src='/images/jtz.jpg'></div>" +
               "<div class='card'><img src='/images/oncjr.jpg'></div>" +
               "<div class='card'><img src='/images/onfzy.jpg'></div>" +
               "<div class='card'><img src='/images/onzt.jpg'></div>" +







               // ══════════════════════════════════ ═════════════════════




               // ══════════════════════════════════ ═════════════════════




               "</div>" +
               "<a href='/logout' class='logout'>🚪 退出</a>" +
               "</body></html>";
    }
}