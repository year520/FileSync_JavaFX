package orz.china.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import orz.china.filesync.beans.FileSyncBean;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class ConfigController extends AnchorPane implements Initializable {

    @FXML
    TextField target;
    @FXML
    TextField include;
    @FXML
    TextField exclude;
    @FXML
    Button targetbtn;
    @FXML
    TableView<String> source;

    @FXML
    Button addbtn;
    @FXML
    Button modifybtn;
    @FXML
    Button deletebtn;

    @FXML
    Label errorMsg;

    private Main app;

    private FileSyncBean sbean;

    private FileSyncBean tbean = new FileSyncBean();

    public void setApp(Main application, FileSyncBean sbean) {
	this.app = application;
	this.sbean = sbean;
	if (null != sbean) {
	    this.tbean = sbean;
	}

	if (null != sbean) {
	    target.setText(tbean.getTargetPath());
	    include.setText(tbean.getIncludeRole());
	    exclude.setText(tbean.getExcludeRole());
	    setSourceView();
	}
	addbtn.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourcebundle) {
    }

    public void targethandle(ActionEvent event) {
	DirectoryChooser dc = new DirectoryChooser();
	dc.setTitle("please choose a directory!");
	if (null != tbean.getTargetPath()) {
	    dc.setInitialDirectory(new File(tbean.getTargetPath()));
	} else {
	    dc.setInitialDirectory(new File(System.getProperty("user.dir")));
	}
	File file = dc.showDialog(null);
	if (null != file) {
	    target.setText(file.getAbsolutePath());
	}
    }

    public void commithandle(ActionEvent event) {
	if (null == target.getText() || "".equals(target.getText())) {
	    errorMsg.setText("target folder must not null..");
	    return;
	}
	if (null == tbean.getSourcePath() || 0 == tbean.getSourcePath().size()) {
	    errorMsg.setText("source folder must not null..");
	    return;
	}
	tbean.setIncludeRole(include.getText());
	tbean.setExcludeRole(exclude.getText());
	tbean.setTargetPath(target.getText());
	if (null == sbean) {
	    app.addBean(tbean);
	} else {
	    app.modifyBean(sbean, tbean);
	}
	try {
	    app.writeConfigFile();
	} catch (IOException e) {
	    errorMsg.setText("writer config file error! please check..");
	}
	app.gotoFileSyncPage();

    }

    public void addhandle(ActionEvent event) {
	DirectoryChooser dc = new DirectoryChooser();
	dc.setTitle("please choose a directory!");
	dc.setInitialDirectory(new File(System.getProperty("user.dir")));
	File file = dc.showDialog(null);
	if (null != file) {
	    if (target.getText().contains(file.getAbsolutePath())) {
		errorMsg.setText("source path  must not same or included in target path..");
		return;
	    }
	    if (tbean.getSourcePath().contains(file.getAbsolutePath())) {
		errorMsg.setText("source path is aleady existing..");
		return;
	    }

	    tbean.getSourcePath().add(file.getAbsolutePath());
	    this.setSourceView();
	}
    }

    public void modifyhandle(ActionEvent event) {
	String s = source.getSelectionModel().getSelectedItem();
	DirectoryChooser dc = new DirectoryChooser();
	dc.setTitle("please choose a directory!");
	dc.setInitialDirectory(new File(s));
	File file = dc.showDialog(null);
	if (null != file) {
	    List<String> il = tbean.getSourcePath();
	    for (int i = 0; i < il.size(); i++) {
		if (s.equals(il.get(i))) {
		    il.set(i, file.getAbsolutePath());
		    break;
		}
	    }
	    this.setSourceView();
	}
    }

    public void deletehandle(ActionEvent event) {
	String s = source.getSelectionModel().getSelectedItem();
	Iterator<String> i = tbean.getSourcePath().iterator();
	while (i.hasNext()) {
	    if (s.equals(i.next())) {
		i.remove();
	    }
	}
	setSourceView();
    }

    public void exithandle(ActionEvent event) {
	app.gotoFileSyncPage();
    }

    private void setSourceView() {
	if (null != source && null != source.getColumns())
	    source.getColumns().clear();
	TableColumn<String, String> sp = new TableColumn<String, String>("source path");
	sp.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
	    public ObservableValue<String> call(CellDataFeatures<String, String> p) {
		return new SimpleStringProperty(p.getValue());
	    }
	});
	sp.setMaxWidth(370);
	sp.setMinWidth(370);
	source.getColumns().add(sp);
	if (null == tbean.getSourcePath()) {
	    return;
	}
	ObservableList<String> list = FXCollections.observableList(tbean.getSourcePath());
	source.setItems(list);
	source.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent arg0) {
		String s = source.getSelectionModel().getSelectedItem();
		if (null != s) {
		    modifybtn.setDisable(false);
		    deletebtn.setDisable(false);
		}
	    }
	});
    }
}
