package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Student;

public class RootController implements Initializable {
	@FXML
	private TableView tableView;
	@FXML
	private TextField txtName;
	@FXML
	private ComboBox cmdLevel;
	@FXML
	private TextField txtBan;
	@FXML
	private Button btnTotal;
	@FXML
	private Button btnAvg;
	@FXML
	private Button btnInit;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnList;
	@FXML
	private TextField txtKo;
	@FXML
	private TextField txtEng;
	@FXML
	private TextField txtMath;
	@FXML
	private TextField txtSic;
	@FXML
	private TextField txtSoc;
	@FXML
	private TextField txtMusic;
	@FXML
	private TextField txtTotal;
	@FXML
	private TextField txtAvg;
	@FXML
	private RadioButton rdoMale;
	@FXML
	private RadioButton rdoFemale;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnBarchart;
	@FXML
	private DatePicker dpDate;
	@FXML
	private ImageView imgView;
	@FXML
	private Button btnImageFile;

	public Stage stage;
	private ObservableList<Student> obsList;
	private ToggleGroup group;
	private int tableViewSelectedIndex;
	private File selectFile;
	private File directorySave;

	public RootController() {
		this.stage = null;
		this.obsList = FXCollections.observableArrayList();
	}

	// �̺�Ʈ ���->�ڵ鷯�Լ� ����, �̺�Ʈ ��� �� ó��, UI��ü�ʱ�ȭ����
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ���̺��UI��ü �÷��ʱ�ȭ����(�÷��� 11���� ����� -> Student Ŭ�����ʵ�� ����)
		tableViewColumnInitialize();
		// �гⷹ���� �Է��ϴ� �ʱ�ȭ ó��
		comboBoxLevelInitialize();
		// ����������ư �׷� �ʱ�ȭ ó��
		radioButtonGenderInitialize();
		// �����Է�â�� 3�ڸ������� �Է¼���(0~100)������ �Է¿��
		textFieldNumberFormat();
		// �����ͺ��̽�(studentDB) ���̺�(gradeTBL) ��系���� ��������
		totalLoadList();
		// �⺻���� �̹��� ���� �����ϱ�
		setDefaultImageView();
		// ������ �����Ҽ� �ִ� ���� �����
		setDirectoryImageSaveImage();

		// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnTotal.setOnAction(event -> handleBtnTotalAction(event));
		// ��չ�ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnAvg.setOnAction(event -> handleBtnAvgAction(event));
		// �ʱ�ȭ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnInit.setOnAction(event -> handleBtnInitAction(event));
		// ��Ϲ�ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnOk.setOnAction(event -> handleBtnOkAction(event));
		// ã���ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnSearch.setOnAction(event -> handleBtnSearchAction(event));
		// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnDelete.setOnAction(event -> handleBtnDeleteAction(event));
		// ���̺�並 ������ ������ �̺�Ʈ��� �ڵ鷯�Լ�ó��
		tableView.setOnMousePressed(event -> handleTableViewPressedAction(event));
		// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnEdit.setOnAction(event -> handleBtnEditAction(event));
		// ����Ʈ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnList.setOnAction(event -> handleBtnListAction(event));
		// ����Ʈ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnBarchart.setOnAction(event -> handleBtnBarChartAction(event));
		// ������Ʈ�̺�Ʈ��� �� �ڵ鷯�Լ�ó��(���̺�� �ι�Ŭ���ϸ� �̺�Ʈ�߻�)
		tableView.setOnMouseClicked(event -> handlePieChartAction(event));
		// �̹�����ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
		btnImageFile.setOnAction(event -> handleBtnImageFileAction(event));

		// �����ư�̺�Ʈ���
		btnExit.setOnAction(event -> stage.close());
		// �⺻���� �Էµ����� ó���Լ�
		insertBasicData();
	}

	private void insertBasicData() {
		txtName.setText("aaa");
		cmdLevel.getSelectionModel().select(1);
		txtBan.setText("2");
		txtKo.setText("90");
		txtMath.setText("90");
		txtEng.setText("80");
		txtMusic.setText("90");
		txtSic.setText("90");
		txtSoc.setText("90");
	}

	// ������Ʈ�̺�Ʈ��� �� �ڵ鷯�Լ�ó��(���̺�� �ι�Ŭ���ϸ� �̺�Ʈ�߻�)
	private void handlePieChartAction(MouseEvent event) {
		// �̺�Ʈ���� �ι��� Ŭ���� �ߴ��� üũ�Ѵ�.
		if (event.getClickCount() != 2)
			return;

		// ��������(��Ÿ��, ���, ���������, �ֽ�������) -> �� -> ȭ�鳻��
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/piechart.fxml"));
			Scene scene = new Scene(root);
			Stage pieChartStage = new Stage(StageStyle.UTILITY);
			// +++++++++++++++++++++++�̺�Ʈ��� �� �ڵ鷯ó��+++++++++++++++++++
			PieChart pieChart = (PieChart) scene.lookup("#pieChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// �ι�Ŭ���� Student ��ü�� ��������
			Student student = obsList.get(tableViewSelectedIndex);

			// ������Ʈ�� �Է��� ������ observable list �Է��Ѵ�.
			ObservableList pieChartList = FXCollections.observableArrayList();
			pieChartList.add(new PieChart.Data("����", Integer.parseInt(student.getKorean())));
			pieChartList.add(new PieChart.Data("����", Integer.parseInt(student.getEnglish())));
			pieChartList.add(new PieChart.Data("����", Integer.parseInt(student.getMath())));
			pieChartList.add(new PieChart.Data("����", Integer.parseInt(student.getSic())));
			pieChartList.add(new PieChart.Data("��ȸ", Integer.parseInt(student.getSoc())));
			pieChartList.add(new PieChart.Data("����", Integer.parseInt(student.getMusic())));

			pieChart.setData(pieChartList);

			btnClose.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					pieChartStage.close();
				}
			});
			// +++++++++++++++++++++++/�̺�Ʈ��� �� �ڵ鷯ó��+++++++++++++++++++
			pieChartStage.initModality(Modality.WINDOW_MODAL);
			pieChartStage.initOwner(stage);
			pieChartStage.setScene(scene);
			pieChartStage.setResizable(false);
			pieChartStage.setTitle("���� ���̱׷���");
			pieChartStage.show();
		} catch (IOException e) {
		}

	}

	// ����Ʈ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnBarChartAction(ActionEvent event) {
		// ���� -> �� -> ��������(��Ÿ��, ���, ���ν�������, ������ũ�⺯��) -> �����ش�.
		try {
			if (obsList.size() == 0)
				throw new Exception();
			Parent root = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Scene scene = new Scene(root);
			Stage barChartStage = new Stage(StageStyle.UTILITY);
			// +++++++++++++++++++++++�̺�Ʈ��� �� �ڵ鷯ó��+++++++++++++++++++
			BarChart barChart = (BarChart) scene.lookup("#barChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// 1. XYChart �ø�� �����. (����)
			XYChart.Series seriesKorean = new XYChart.Series();
			seriesKorean.setName("����");
			ObservableList koreanList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				koreanList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesKorean.setData(koreanList);
			barChart.getData().add(seriesKorean);

			// 1. XYChart �ø�� �����. (����)
			XYChart.Series seriesEnglish = new XYChart.Series();
			seriesEnglish.setName("����");
			ObservableList englishList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				englishList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getEnglish())));
			}
			seriesEnglish.setData(englishList);
			barChart.getData().add(seriesEnglish);

			// 1. XYChart �ø�� �����. (����)
			XYChart.Series seriesMath = new XYChart.Series();
			seriesMath.setName("����");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				mathList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getMath())));
			}
			seriesMath.setData(mathList);
			barChart.getData().add(seriesMath);

			// 1. XYChart �ø�� �����. (����)
			XYChart.Series seriesSic = new XYChart.Series();
			seriesSic.setName("����");
			ObservableList sicList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				sicList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getSic())));
			}
			seriesSic.setData(sicList);
			barChart.getData().add(seriesSic);

			// 1. XYChart �ø�� �����. (��ȸ)
			XYChart.Series seriesSoc = new XYChart.Series();
			seriesSoc.setName("��ȸ");
			ObservableList socList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				socList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getSoc())));
			}
			seriesSoc.setData(socList);
			barChart.getData().add(seriesSoc);

			// 1. XYChart �ø�� �����. (����)
			XYChart.Series seriesMusic = new XYChart.Series();
			seriesMusic.setName("����");
			ObservableList musicList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				musicList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getMusic())));
			}
			seriesMusic.setData(musicList);
			barChart.getData().add(seriesMusic);

			// +++++++++++++++++++++++/�̺�Ʈ��� �� �ڵ鷯ó��+++++++++++++++++++
			barChartStage.initModality(Modality.WINDOW_MODAL);
			barChartStage.initOwner(stage);
			barChartStage.setScene(scene);
			barChartStage.setResizable(false);
			barChartStage.setTitle("���� ����׷���");
			barChartStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���̺�� ����Ʈ �Է¿��!");
			alert.setHeaderText("����Ÿ����Ʈ�� �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���.");
			alert.showAndWait();
		}
	}

	// �����Է�â�� 3�ڸ������� �Է¼���(0~100)������ �Է¿��
	private void textFieldNumberFormat() {
		// 10���� 3�ڸ������� �Է����� �����ϴ� ��ü
		DecimalFormat decimalFormat = new DecimalFormat("###");

		txtKo.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtEng.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtMath.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtSic.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtSoc.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtMusic.setTextFormatter(new TextFormatter<>(e -> {
			// 1. �����Է��� �����̽������̸� �ٽ� �̺�Ʈ�� �����ش�.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. ��ġ����(Ű����ġ�� ��ġ�����ذ���.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. ���ڸ� �ްڴ�.(3���ڸ�)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�����Է¿��!");
				alert.setHeaderText("����(0~100)�� �Է��Ͻÿ�.");
				alert.setContentText("���ڿ��� �ٸ������Էµ�����������.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));
	}

	// ���̺��UI��ü �÷��ʱ�ȭ����(�÷��� 11���� ����� -> Student Ŭ�����ʵ�� ����)
	private void tableViewColumnInitialize() {
		TableColumn colNo = new TableColumn("NO.");
		colNo.setMaxWidth(40);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));
		colNo.setStyle("-Fx-alignment: CENTER");

		TableColumn colName = new TableColumn("����");
		colName.setMaxWidth(60);
		colName.setCellValueFactory(new PropertyValueFactory("name"));
		colName.setStyle("-Fx-alignment: CENTER");

		TableColumn colLevel = new TableColumn("�г�");
		colLevel.setMaxWidth(50);
		colLevel.setCellValueFactory(new PropertyValueFactory("level"));
		colLevel.setStyle("-Fx-alignment: CENTER");

		TableColumn colBan = new TableColumn("��");
		colBan.setMaxWidth(40);
		colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));
		colBan.setStyle("-Fx-alignment: CENTER");

		TableColumn colGender = new TableColumn("����");
		colGender.setMaxWidth(40);
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colGender.setStyle("-Fx-alignment: CENTER");

		TableColumn colKorean = new TableColumn("����");
		colKorean.setMaxWidth(40);
		colKorean.setCellValueFactory(new PropertyValueFactory<>("korean"));
		colKorean.setStyle("-Fx-alignment: CENTER");

		TableColumn colEnglish = new TableColumn("����");
		colEnglish.setMaxWidth(40);
		colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));
		colEnglish.setStyle("-Fx-alignment: CENTER");

		TableColumn colMath = new TableColumn("����");
		colMath.setMaxWidth(40);
		colMath.setCellValueFactory(new PropertyValueFactory<>("math"));
		colMath.setStyle("-Fx-alignment: CENTER");

		TableColumn colSic = new TableColumn("����");
		colSic.setMaxWidth(40);
		colSic.setCellValueFactory(new PropertyValueFactory<>("sic"));
		colSic.setStyle("-Fx-alignment: CENTER");

		TableColumn colSoc = new TableColumn("��ȸ");
		colSoc.setMaxWidth(40);
		colSoc.setCellValueFactory(new PropertyValueFactory<>("soc"));
		colSoc.setStyle("-Fx-alignment: CENTER");

		TableColumn colMusic = new TableColumn("����");
		colMusic.setMaxWidth(40);
		colMusic.setCellValueFactory(new PropertyValueFactory<>("music"));
		colMusic.setStyle("-Fx-alignment: CENTER");

		TableColumn colTotal = new TableColumn("����");
		colTotal.setMaxWidth(50);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		colTotal.setStyle("-Fx-alignment: CENTER");

		TableColumn colAvg = new TableColumn("���");
		colAvg.setMaxWidth(50);
		colAvg.setCellValueFactory(new PropertyValueFactory<>("avg"));
		colAvg.setStyle("-Fx-alignment: CENTER");

		TableColumn colRegister = new TableColumn("�����");
		colRegister.setMaxWidth(110);
		colRegister.setCellValueFactory(new PropertyValueFactory<>("register"));

		TableColumn colFilename = new TableColumn("�̹���");
		colFilename.setMaxWidth(400);
		colFilename.setCellValueFactory(new PropertyValueFactory<>("filename"));

		tableView.getColumns().addAll(colNo, colName, colLevel, colBan, colGender, colKorean, colEnglish, colMath,
				colSic, colSoc, colMusic, colTotal, colAvg, colRegister, colFilename);
		tableView.setItems(obsList);
	}

	// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnTotalAction(ActionEvent event) {
		try {
			int korean = Integer.parseInt(txtKo.getText());
			int english = Integer.parseInt(txtEng.getText());
			int math = Integer.parseInt(txtMath.getText());
			int sic = Integer.parseInt(txtSic.getText());
			int soc = Integer.parseInt(txtSoc.getText());
			int music = Integer.parseInt(txtMusic.getText());
			int total = korean + english + math + sic + soc + music;
			txtTotal.setText(String.valueOf(total));
		} catch (NumberFormatException e) {
			// ���â�� �������(��������, ��, ȭ�鳻�� �� �������)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�����Է¿��!");
			alert.setHeaderText("������ �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���.");
			alert.showAndWait();
		}
	}

	// ��չ�ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnAvgAction(ActionEvent event) {
		try {
			double avg = Integer.parseInt(txtTotal.getText()) / 6.0;
			txtAvg.setText(String.format("%.1f", avg));
		} catch (NumberFormatException e) {
			// ���â�� �������(��������, ��, ȭ�鳻�� �� �������)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�����Է¿��!");
			alert.setHeaderText("������ �Է��� �ȵƾ��.");
			alert.setContentText("�������� �����ϼ���.");
			alert.showAndWait();
		}
	}

	// �ʱ�ȭ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnInitAction(ActionEvent event) {
		txtName.clear();
		cmdLevel.getSelectionModel().clearSelection();
		rdoMale.setSelected(false);
		rdoFemale.setSelected(false);
		txtBan.clear();
		txtKo.clear();
		txtEng.clear();
		txtMath.clear();
		txtSic.clear();
		txtSoc.clear();
		txtMusic.clear();
		txtTotal.clear();
		txtAvg.clear();
	}

	// ��Ϲ�ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnOkAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// �̹�������ó������ 1. �̹������ϸ��� �����ؼ� �����ؼ� �ش���丮�� �����Ѵ�.
		// 1. �̹������ϸ��� �������Ѵ�.
		if (selectFile == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�������þ���!");
			alert.setHeaderText("������������ �����߻�");
			alert.setContentText("�̹������ϸ� �����ϼ���!");
			alert.showAndWait();
			return;
		}
		// 2. ���������� �����ͼ� ���θ��� �̹��� ���ϸ� �����Ѵ�.
		BufferedInputStream bis = null; // ������ ������ ����ϴ� Ŭ����
		BufferedOutputStream bos = null; // ������ ���� ����ϴ� Ŭ����
		String fileName = null;
		try {
			// "stu1238452179ȫ�浿.jpg"
			fileName = "stu" + System.currentTimeMillis() + selectFile.getName();

			// �̹��������� ����Ʈ��Ʈ������ �ٲپ ���۸� Ȱ���ؼ� �д´�.
			// C:/images/stu1238452179ȫ�浿.jpg
			bis = new BufferedInputStream(new FileInputStream(selectFile));
			bos = new BufferedOutputStream(new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
			// �̹��������� �о ������ġ�� �ִ� ���Ͽ��ٰ� ����.
			// -1 : ���̻� �������� ���ٴ� �ǹ��̴�.
			int data = -1;
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush(); // ���ۿ� �ִ°��� �� �����ϱ����ؼ� ������.
			}
		} catch (Exception e) {
			System.out.println("���Ϻ��翡��" + e.getMessage());
			return;
		} finally {
				try {
					if (bis != null)	bis.close();
					if (bos != null) bis.close();
				} catch (IOException e) {
					System.out.println("bis.close(), bos.close() error!"+e.getMessage());
				}
		}
		if(dpDate.getValue().toString().trim().equals("")) {
			System.out.println("��¥�� �Է����ּ���.");
			return;
		}
		// 5. ����ڵ带 ���������� �ش�� �������� ? ��ȣ�� �����Ѵ�.
		Student student = new Student(txtName.getText(), cmdLevel.getSelectionModel().getSelectedItem().toString(),
				txtBan.getText(), ((RadioButton) group.getSelectedToggle()).getText(), txtKo.getText(),
				txtEng.getText(), txtMath.getText(), txtSic.getText(), txtSoc.getText(), txtMusic.getText(),
				txtTotal.getText(), txtAvg.getText(), dpDate.getValue().toString(), fileName);
		
		int returnValue = studentDAO.getStudentRegistry(student);
		
		if (returnValue != 0) {
			obsList.clear();
			totalLoadList();
			setDefaultImageView();
		}
		
	}

	// �гⷹ���� �Է��ϴ� �ʱ�ȭ ó��
	private void comboBoxLevelInitialize() {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		obsList.addAll("1�г�", "2�г�", "3�г�", "4�г�", "5�г�", "6�г�");
		cmdLevel.setItems(obsList);
	}

	// ����������ư �׷� �ʱ�ȭ ó��
	private void radioButtonGenderInitialize() {
		group = new ToggleGroup();
		rdoMale.setToggleGroup(group);
		rdoFemale.setToggleGroup(group);
		rdoMale.setSelected(true);
	}

	// ã���ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnSearchAction(ActionEvent event) {
		try {
			StudentDAO studentDAO = new StudentDAO();
			if (txtSearch.getText().trim().equals("")) {
				throw new Exception();
			}
			ArrayList<Student> arrayList = studentDAO.getStudentFind(txtSearch.getText().trim());

			if (arrayList.size() != 0) {
				obsList.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					Student s = arrayList.get(i);
					obsList.add(s);
				}
			}
		} catch (Exception e) {
			// ���â�� �������(��������, ��, ȭ�鳻�� �� �������)
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("����Է¹����߻�!");
			alert.setHeaderText("Student ��ü ������ ���ּ���.");
			alert.setContentText("�������� �����ϼ���.");
			alert.showAndWait();
		}
	}

	// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnDeleteAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// 5. ����ڵ带 ���������� �ش�� �������� ? ��ȣ�� �����Ѵ�
		Student student = obsList.get(tableViewSelectedIndex);
		int no = student.getNo();
		int returnValue = studentDAO.getStudentDelete(no);

		if (returnValue != 0) {
			//�̹��� �������� ��������� �� ������ �����ؾߵȴ�.
			//1. ������ �̹��� ���ϸ��� �����´�.
			String filename = student.getFilename();
			File fileDelete = new File(directorySave.getAbsolutePath()+"\\"+filename);
			
			//2. ��¥�� ������ �ְ�, ���� �̹��� �������� Ȯ���Ѵ�.
			if(fileDelete.exists() && fileDelete.isFile()) {
				fileDelete.delete();
			}
			obsList.remove(tableViewSelectedIndex);
		}
	}

	// ���̺�並 ������ ������ �̺�Ʈ��� �ڵ鷯�Լ�ó��
	private void handleTableViewPressedAction(MouseEvent event) {
		tableViewSelectedIndex = tableView.getSelectionModel().getSelectedIndex();
	}

	// ������ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnEditAction(ActionEvent event) {
		// formEdit.fxml ȭ���� �ε��ؾߵȴ�.
		try {
			// ȭ�鳻��->��->��������(���ν�������)->�����ָ�ȴ�.�� �����Ծ��.
			Parent root = FXMLLoader.load(getClass().getResource("/view/formEdit.fxml"));
			// scene(ȭ�鳻��) �����.
			Scene scene = new Scene(root);
			Stage editStage = new Stage(StageStyle.UTILITY);
			// ++++++++++++++++++++++++�̺�Ʈ��� �� �ڵ鷯 ó��+++++++++++++++++++++++
			// @FXML private TextField txtName -> �̰��� �Ҽ�����. (��Ʈ�ѷ��� ���⶧��)
			TextField txtNo = (TextField) scene.lookup("#txtNo");
			TextField txtName = (TextField) scene.lookup("#txtName");
			TextField txtYear = (TextField) scene.lookup("#txtYear");
			TextField txtBan = (TextField) scene.lookup("#txtBan");
			TextField txtGender = (TextField) scene.lookup("#txtGender");
			TextField txtKorean = (TextField) scene.lookup("#txtKorean");
			TextField txtEnglish = (TextField) scene.lookup("#txtEnglish");
			TextField txtMath = (TextField) scene.lookup("#txtMath");
			TextField txtSic = (TextField) scene.lookup("#txtSic");
			TextField txtSoc = (TextField) scene.lookup("#txtSoc");
			TextField txtMusic = (TextField) scene.lookup("#txtMusic");
			TextField txtTotal = (TextField) scene.lookup("#txtTotal");
			TextField txtAvg = (TextField) scene.lookup("#txtAvg");
			Button btnCal = (Button) scene.lookup("#btnCal");
			Button btnFormAdd = (Button) scene.lookup("#btnFormAdd");
			Button btnFormCancel = (Button) scene.lookup("#btnFormCancel");

			// ���̺�信�� ���õ� ��ġ���� ������ observablelist���� �� ��ġ�� ã�Ƽ� �ش�� student ��ü��
			// ��������ȴ�.
			Student student = obsList.get(tableViewSelectedIndex);
			txtNo.setText(String.valueOf(student.getNo()));
			txtName.setText(student.getName());
			txtYear.setText(student.getLevel());
			txtBan.setText(student.getBan());
			txtGender.setText(student.getGender());
			txtKorean.setText(student.getKorean());
			txtEnglish.setText(student.getEnglish());
			txtMath.setText(student.getMath());
			txtSic.setText(student.getSic());
			txtSoc.setText(student.getSoc());
			txtMusic.setText(student.getMusic());
			txtTotal.setText(student.getTotal());
			txtAvg.setText(student.getAvg());
			// txtNo �ؽ�Ʈ�ʵ带 read only(�б⸸ ����) �����.(��ȣ, �̸�, �г�, ��, ����)
			txtNo.setDisable(true);
			txtName.setDisable(true);
			txtYear.setDisable(true);
			txtBan.setDisable(true);
			txtGender.setDisable(true);
			// ����ư�� �ش�� �̺�Ʈ��� �� �ڵ鷯 ó�����
			btnCal.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int korean = Integer.parseInt(txtKorean.getText());
					int english = Integer.parseInt(txtEnglish.getText());
					int math = Integer.parseInt(txtMath.getText());
					int sic = Integer.parseInt(txtSic.getText());
					int soc = Integer.parseInt(txtSoc.getText());
					int music = Integer.parseInt(txtMusic.getText());
					int total = korean + english + math + sic + soc + music;
					txtTotal.setText(String.valueOf(total));

					double avg = Integer.parseInt(txtTotal.getText()) / 6.0;
					txtAvg.setText(String.format("%.1f", avg));
				}
			});
			// �����ư �̺�Ʈ��� �� �ڵ鷯 ó�����
			btnFormAdd.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Student stu = obsList.get(tableViewSelectedIndex);
					stu.setKorean(txtKorean.getText());
					stu.setEnglish(txtEnglish.getText());
					stu.setMath(txtMath.getText());
					stu.setSic(txtSic.getText());
					stu.setSoc(txtSoc.getText());
					stu.setMusic(txtMusic.getText());
					stu.setTotal(txtTotal.getText());
					stu.setAvg(txtAvg.getText());

					StudentDAO studentDAO = new StudentDAO();
					int returnValue = studentDAO.getStudentUpdate(stu);
					if (returnValue != 0) {
						// ���̺�� obsList �ش�� ��ġ�� ������ ��ü���� �Է��Ѵ�.
						obsList.set(tableViewSelectedIndex, stu);
					}
				}
			});

			btnFormCancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					editStage.close();
				}
			});

			// ++++++++++++++++++++++++/�̺�Ʈ��� �� �ڵ鷯 ó��+++++++++++++++++++++++
			// ��������(���ν�������)�� �����. (*���, ��޸���), ��������(��)
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(stage);
			editStage.setScene(scene);
			editStage.setTitle("�������α׷� ����â");
			editStage.setResizable(false);
			editStage.show();
		} catch (IndexOutOfBoundsException | IOException e) {
			// ���â�� �������(��������, ��, ȭ�鳻�� �� �������)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("����â ����!");
			alert.setHeaderText("���˿��");
			alert.setContentText("������������!");
			alert.showAndWait();
		}
	}

	// �����ͺ��̽�(studentDB) ���̺�(gradeTBL) ��系���� ��������
	private void totalLoadList() {
		StudentDAO studentDAO = new StudentDAO();
		ArrayList<Student> arrayList = studentDAO.getTotalLoadList();
		if (arrayList == null) {
			return;
		}
		for (int i = 0; i < arrayList.size(); i++) {
			Student s = arrayList.get(i);
			obsList.add(s);
		}
	}

	// ����Ʈ��ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnListAction(ActionEvent event) {
		obsList.clear();
		totalLoadList();
	}

	// �⺻���� �̹��� ���� �����ϱ�
	private void setDefaultImageView() {
		Image image = new Image("/image/default.jpg", false);
		imgView.setImage(image);
	}

	// �̹�����ư�̺�Ʈ��� �� �ڵ鷯�Լ�ó��
	private void handleBtnImageFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		// ���õ� �̹��� ������ ������������ �����ش�.
		selectFile = fileChooser.showOpenDialog(stage);
		selectFile.getName();
		try {
			if (selectFile != null) {
				// ������ ������ΰ� ���ڿ��� ��ȯ�ȴ�.
				String localURL = selectFile.toURI().toURL().toString();
				Image image = new Image(localURL, false);
				imgView.setImage(image);
			}
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�̹�����ư ����!");
			alert.setHeaderText("�̹�����ư ���˿��");
			alert.setContentText("�̹������ϸ� �������ñ� �ٶ��ϴ�.");
			alert.showAndWait();
		}
	}

	// ������ �����Ҽ� �ִ� ���� ����� ("c:/images")
	private void setDirectoryImageSaveImage() {
		directorySave = new File("C:/images");
		if (!directorySave.exists()) {
			directorySave.mkdir();
			System.out.println("C:/images ���丮 �̹��� ����� ����!");
		}
	}

}
