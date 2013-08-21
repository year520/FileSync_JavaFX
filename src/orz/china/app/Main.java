/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orz.china.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import orz.china.filesync.beans.FileSyncBean;
import orz.china.filesync.beans.FileSyncProp;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author bo.chen
 */
public class Main extends Application {

    private static Logger log = Logger.getLogger(Main.class);
    private Stage stage;

    private FileSyncProp prop;

    public File getconfigFile() {
	File file = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + "config.xml");
	log.info(file.getAbsoluteFile());
	return file;
    }

    public void writeConfigFile() throws IOException {
	XStream xs = new XStream();
	xs.processAnnotations(FileSyncProp.class);
	String xml = xs.toXML(prop);
	log.info(xml);
	FileUtils.writeStringToFile(this.getconfigFile(), xml);
    }

    public FileSyncProp getFileSyncProp() {
	log.info(String.valueOf(prop));
	if (null != prop)
	    return prop;
	File file = this.getconfigFile();
	if (null != file && file.exists()) {
	    XStream xs = new XStream();
	    xs.processAnnotations(FileSyncProp.class);
	    prop = (FileSyncProp) xs.fromXML(file);
	} else {
	    prop = new FileSyncProp();
	}
	return prop;
    }

    @Override
    public void start(Stage stage) throws Exception {
	log.info("start.....");
	this.stage = stage;
	stage.setTitle("FileSync for pig");
	stage.centerOnScreen();
	stage.getIcons().add(new Image(Main.class.getResourceAsStream("1.png")));
	gotoFileSyncPage();
	stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
	launch(Main.class);
    }

    public void gotoFileSyncPage() {
	try {
	    FileSyncController profile = (FileSyncController) replaceSceneContent("filesync.fxml");
	    profile.setApp(this);
	} catch (Exception ex) {
	    log.info("gotoFileSyncPage error", ex);
	}
    }

    public void gotoConfigPage(FileSyncBean bean) {
	try {
	    ConfigController profile = (ConfigController) replaceSceneContent("manage.fxml");
	    profile.setApp(this, bean);
	} catch (Exception ex) {
	    log.info("gotoConfigPage error", ex);
	}
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
	FXMLLoader loader = new FXMLLoader();
	InputStream in = Main.class.getResourceAsStream(fxml);
	loader.setBuilderFactory(new JavaFXBuilderFactory());
	loader.setLocation(Main.class.getResource(fxml));
	AnchorPane page;
	try {
	    page = (AnchorPane) loader.load(in);
	} finally {
	    in.close();
	}
	Scene scene = new Scene(page);
	stage.setScene(scene);
	stage.sizeToScene();
	return (Initializable) loader.getController();
    }

    public void modifyBean(FileSyncBean sbean, FileSyncBean tbean) {
	List<FileSyncBean> beans = prop.getBeans();
	for (int i = 0; i < beans.size(); i++) {
	    if (sbean.toString().equals(beans.get(i)))
		beans.set(i, tbean);
	}
	log.info("modify bean:" + String.valueOf(beans));
	prop.setBeans(beans);
    }

    public void addBean(FileSyncBean bean) {
	prop.addBean(bean);
	log.info("add bean:" + String.valueOf(bean));
    }

    public void deleteBean(FileSyncBean bean) {
	List<FileSyncBean> beans = prop.getBeans();
	Iterator<FileSyncBean> i = beans.iterator();
	while (i.hasNext()) {
	    if (bean.toString().equals(i.next().toString())) {
		log.info("delete bean:" + String.valueOf(bean));
		i.remove();
	    }
	}
	prop.setBeans(beans);
    }
}
