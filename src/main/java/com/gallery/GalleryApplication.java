package com.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class GalleryApplication {

    // ============================================================
    //  📌 配置区（改这里就行）
    // ============================================================
    private static final String PASSWORD = "1234";   // 密码改这里
    private static final String IMAGE_DIR = "src/main/resources/static/images/";

    public static void main(String[] args) {
        SpringApplication.run(GalleryApplication.class, args);
    }

    // ============================================================
    //  首页
    // ============================================================
    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("login") == null) {
            return loginPage(null);
        }
        return galleryPage();
    }

    @GetMapping("/login")
    public String loginPage() {
        return loginPage(null);
    }

    @GetMapping("/check")
    public String check(@RequestParam String pwd, HttpSession session) {
        if (PASSWORD.equals(pwd)) {
            session.setAttribute("login", true);
            return "redirect:/";
        }
        return loginPage("❌ 密码错误");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ============================================================
    //  图片访问
    // ============================================================
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(IMAGE_DIR + filename).normalize();
            Resource res = new UrlResource(path.toUri());
            if (res.exists() && res.isReadable()) {
                return ResponseEntity.ok().body(res);
            }
            return ResponseEntity.notFound().build();
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ============================================================
    //  页面生成
    // ============================================================

    private String loginPage(String error) {
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

    private String galleryPage() {
        List<String> images = getImageList();
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>图片展示</title>");
        html.append("<style>");
        html.append("body{background:#0f172a;color:#fff;font-family:system-ui;padding:30px;text-align:center;margin:0}");
        html.append("h1{font-size:28px;font-weight:500}");
        html.append(".gallery{display:grid;grid-template-columns:repeat(auto-fill,minmax(250px,1fr));gap:20px;max-width:1000px;margin:20px auto}");
        html.append(".card{background:rgba(255,255,255,0.05);border-radius:16px;overflow:hidden;border:1px solid rgba(255,255,255,0.06)}");
        html.append(".card img{width:100%;aspect-ratio:1/1;object-fit:cover;display:block}");
        html.append(".empty{color:#64748b;padding:60px 20px;font-size:18px}");
        html.append(".logout{display:inline-block;margin-top:20px;padding:10px 30px;background:#ef4444;color:#fff;border-radius:30px;text-decoration:none}");
        html.append("</style></head><body>");
        html.append("<h1>📸 图片展示</h1>");

        if (images.isEmpty()) {
            html.append("<div class='empty'>📭 暂无图片，请把图片放到 static/images/</div>");
        } else {
            html.append("<div class='gallery'>");
            for (String img : images) {
                html.append("<div class='card'><img src='/images/").append(img).append("' alt='").append(img).append("'></div>");
            }
            html.append("</div>");
        }

        html.append("<a href='/logout' class='logout'>🚪 退出</a>");
        html.append("</body></html>");
    }

    private List<String> getImageList() {
        List<String> list = new ArrayList<>();
        File dir = new File(IMAGE_DIR);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) list.add(f.getName());
                }
            }
        }
        return list;
    }
}