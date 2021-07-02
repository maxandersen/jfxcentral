package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.util.PageUtil;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.IOException;


public class WebApp extends com.jpro.web.WebApp {

    WebApp(Stage stage) {
        super(stage);

        addRouteJava((s) -> {
            if (s.equals("") || s.equals("/")) {
                return new com.jpro.web.Redirect("/home");
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if (s.startsWith("/memory")) {
                return new MemoryView();
            } else {
                return null;
            }
        });

        addRouteJava((s) -> {
            if (s.startsWith("/refresh")) {
                try {
                    JFXCentralApp.updateRepository(new TextProgressMonitor());
                    DataRepository.getInstance().refreshData();
                } catch (GitAPIException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return new com.jpro.web.Redirect("/home");
                }
            }
            return null;
        });

        addRouteJava((s) -> {
            if (s.startsWith("/")) {
                if (PageUtil.getViewFromURL(s) != null) {
                    WebAPI webAPI = webAPI();
                    boolean mobile = false;
                    if (webAPI != null) {
                        Rectangle2D browserSize = webAPI.getBrowserSize();
                        mobile = browserSize.getWidth() < 800;
                        System.out.println("browserSize: " + browserSize);
                        System.out.println("--------");
                    }
                    return new WebView(s, mobile);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });

        addRouteJava((s) -> new ErrorView(s));
    }
}