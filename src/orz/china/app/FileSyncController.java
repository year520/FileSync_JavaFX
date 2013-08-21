/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orz.china.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import orz.china.filesync.IFileSync;
import orz.china.filesync.beans.FileSyncBean;
import orz.china.filesync.beans.FileSyncProp;
import orz.china.filesync.impl.FileSyncImpl;
import orz.china.filesync.util.FileSyncUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author bo.chen
 */
public class FileSyncController extends AnchorPane implements Initializable {
    private static Logger log = Logger.getLogger(FileSyncController.class);
    @FXML
    TextField configPath;
    @FXML
    Button browsebtn;
    @FXML
    Button addbtn;
    @FXML
    Button modifybtn;
    @FXML
    Button deletebtn;
    @FXML
    Button monitoringbtn;
    @FXML
    Button stopbtn;
    @FXML
    Button syncAllbtn;
    @FXML
    Label errorMsg;
    @FXML
    TableView<FileSyncBean> config;

    private IFileSync sync;

    private Main app;

    public void setApp(Main application) {
	this.app = application;
	try {
	    setTableView();
	    initButton(true);
	} catch (Exception e) {
	    errorMsg.setText("error config file! please check the file:" + app.getconfigFile().getAbsolutePath());
	    log.error("error config file! please check the file", e);
	}
	addbtn.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void browsehandle(ActionEvent event) {
	FileChooser fc = new FileChooser();
	fc.setTitle("please select xml file");
	fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xml", "*.xml"), new FileChooser.ExtensionFilter("xml", "*.XML"));
	if (null != configPath.getText() && "".equals(configPath.getText())) {
	    fc.setInitialDirectory(new File(configPath.getText()));
	} else {
	    fc.setInitialDirectory(new File(System.getProperty("user.dir")));
	}
	File file = fc.showOpenDialog(null);
	if (checkFile(file)) {
	    configPath.setText(file.getAbsolutePath());
	    FileSyncUtil.copyFile(file, app.getconfigFile());
	    this.setApp(app);
	} else {
	    errorMsg.setText("error file structure! please select another file..");
	    log.info("error file structure! please select another file..");
	}
    }

    private boolean checkFile(File file) {
	if (null == file)
	    return false;
	try {
	    XStream xs = new XStream();
	    xs.processAnnotations(FileSyncProp.class);
	    @SuppressWarnings("unused")
	    FileSyncProp prop = (FileSyncProp) xs.fromXML(file);
	} catch (Exception e) {
	    log.error("check file error", e);
	    return false;
	}
	return true;
    }

    public void addhandle(ActionEvent event) {
	app.gotoConfigPage(null);
    }

    public void modifyhandle(ActionEvent event) {
	app.gotoConfigPage(config.getSelectionModel().getSelectedItem());
    }

    public void deletehandle(ActionEvent event) {
	FileSyncBean s = config.getSelectionModel().getSelectedItem();
	app.deleteBean(s);
	this.setTableView();
	try {
	    app.writeConfigFile();
	} catch (IOException e) {
	    errorMsg.setText("writer config file error! please check..");
	    log.info("writer config file error! please check..", e);
	}

    }

    public void monitoringhandle(ActionEvent event) {
	if (!checkConfigData()) {
	    errorMsg.setText("config data must not null !..");
	    return;
	}
	sync = new FileSyncImpl(app.getFileSyncProp());
	try {
	    initButton(false);
	    sync.monitorAndSyncFile();
	} catch (Exception e) {
	    errorMsg.setText("monitoring file error! please check...");
	}
    }

    private void initButton(boolean flag) {
	browsebtn.setDisable(!flag);
	config.setDisable(!flag);
	addbtn.setDisable(!flag);
	modifybtn.setDisable(true);
	deletebtn.setDisable(true);
	syncAllbtn.setDisable(!flag);
	monitoringbtn.setDisable(!flag);
	stopbtn.setDisable(flag);
    }

    public void syncAllhandle(ActionEvent event) {
	if (!checkConfigData()) {
	    errorMsg.setText("config data must not null !..");
	    return;
	}
	sync = new FileSyncImpl(app.getFileSyncProp());
	try {
	    initButton(false);
	    sync.syncAllFile();
	    initButton(true);
	} catch (Exception e) {
	    errorMsg.setText("Sync file error! please check...");
	    initButton(true);
	}
    }

    private boolean checkConfigData() {
	if (null == app.getFileSyncProp() || null == app.getFileSyncProp().getBeans() || 0 == app.getFileSyncProp().getBeans().size())
	    return false;
	return true;
    }

    public void stophandle(ActionEvent event) {
	if (null != sync) {
	    try {
		sync.stop();
		initButton(true);
	    } catch (Exception e) {
		errorMsg.setText("Stop monitor error!");
	    }
	}
    }

    private void setTableView() {

	if (null != config.getColumns())
	    config.getColumns().clear();
	TableColumn<FileSyncBean, String> target = new TableColumn<FileSyncBean, String>("target");
	target.setCellValueFactory(new PropertyValueFactory<FileSyncBean, String>("targetPath"));
	target.setPrefWidth(150);
	config.getColumns().add(target);

	TableColumn<FileSyncBean, String> source = new TableColumn<FileSyncBean, String>("source");

	source.setCellValueFactory(new Callback<CellDataFeatures<FileSyncBean, String>, ObservableValue<String>>() {
	    public ObservableValue<String> call(CellDataFeatures<FileSyncBean, String> p) {
		List<String> ss = p.getValue().getSourcePath();
		String sv = "";
		for (int i = 0; i <= ss.size() - 1; i++) {
		    if (i < ss.size() && i != 0) {
			sv += ";";
		    }
		    sv += ss.get(i);
		}
		return new SimpleStringProperty(sv);
	    }
	});
	source.setPrefWidth(158);
	config.getColumns().add(source);

	TableColumn<FileSyncBean, String> include = new TableColumn<FileSyncBean, String>("include");
	include.setCellValueFactory(new PropertyValueFactory<FileSyncBean, String>("includeRole"));
	include.setPrefWidth(100);
	config.getColumns().add(include);

	TableColumn<FileSyncBean, String> exclude = new TableColumn<FileSyncBean, String>("exclude");
	exclude.setCellValueFactory(new PropertyValueFactory<FileSyncBean, String>("excludeRole"));
	exclude.setPrefWidth(100);
	config.getColumns().add(exclude);

	config.setMinWidth(510);
	config.setMaxWidth(510);

	FileSyncProp fileSync = app.getFileSyncProp();
	if (null == fileSync || null == fileSync.getBeans()) {
	    return;
	}
	ObservableList<FileSyncBean> list = FXCollections.observableList(fileSync.getBeans());
	config.setItems(list);
	config.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent arg0) {
		FileSyncBean bean = config.getSelectionModel().getSelectedItem();
		if (null != bean) {
		    modifybtn.setDisable(false);
		    addbtn.setDisable(false);
		    deletebtn.setDisable(false);
		}
	    }
	});

    }
}
