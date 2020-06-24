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

	// 이벤트 등록->핸들러함수 연결, 이벤트 등록 및 처리, UI객체초기화셋팅
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 테이블뷰UI객체 컬럼초기화셋팅(컬럼을 11개를 만들고 -> Student 클래스필드와 연결)
		tableViewColumnInitialize();
		// 학년레벨을 입력하는 초기화 처리
		comboBoxLevelInitialize();
		// 성별라디오버튼 그룹 초기화 처리
		radioButtonGenderInitialize();
		// 점수입력창에 3자리수까지 입력셋팅(0~100)점수만 입력요망
		textFieldNumberFormat();
		// 데이터베이스(studentDB) 테이블(gradeTBL) 모든내용을 가져오기
		totalLoadList();
		// 기본적인 이미지 사진 세팅하기
		setDefaultImageView();
		// 사진을 저장할수 있는 폴더 만들기
		setDirectoryImageSaveImage();

		// 총점버튼이벤트등록 및 핸들러함수처리
		btnTotal.setOnAction(event -> handleBtnTotalAction(event));
		// 평균버튼이벤트등록 및 핸들러함수처리
		btnAvg.setOnAction(event -> handleBtnAvgAction(event));
		// 초기화버튼이벤트등록 및 핸들러함수처리
		btnInit.setOnAction(event -> handleBtnInitAction(event));
		// 등록버튼이벤트등록 및 핸들러함수처리
		btnOk.setOnAction(event -> handleBtnOkAction(event));
		// 찾기버튼이벤트등록 및 핸들러함수처리
		btnSearch.setOnAction(event -> handleBtnSearchAction(event));
		// 삭제버튼이벤트등록 및 핸들러함수처리
		btnDelete.setOnAction(event -> handleBtnDeleteAction(event));
		// 테이블뷰를 선택을 했을때 이벤트등록 핸들러함수처리
		tableView.setOnMousePressed(event -> handleTableViewPressedAction(event));
		// 수정버튼이벤트등록 및 핸들러함수처리
		btnEdit.setOnAction(event -> handleBtnEditAction(event));
		// 리스트버튼이벤트등록 및 핸들러함수처리
		btnList.setOnAction(event -> handleBtnListAction(event));
		// 바차트버튼이벤트등록 및 핸들러함수처리
		btnBarchart.setOnAction(event -> handleBtnBarChartAction(event));
		// 파이차트이벤트등록 및 핸들러함수처리(테이블뷰 두번클릭하면 이벤트발생)
		tableView.setOnMouseClicked(event -> handlePieChartAction(event));
		// 이미지버튼이벤트등록 및 핸들러함수처리
		btnImageFile.setOnAction(event -> handleBtnImageFileAction(event));

		// 종료버튼이벤트등록
		btnExit.setOnAction(event -> stage.close());
		// 기본적인 입력데이터 처리함수
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

	// 파이차트이벤트등록 및 핸들러함수처리(테이블뷰 두번클릭하면 이벤트발생)
	private void handlePieChartAction(MouseEvent event) {
		// 이벤트에서 두번을 클릭을 했는지 체크한다.
		if (event.getClickCount() != 2)
			return;

		// 스테이지(스타일, 모달, 사이즈고정, 주스테이지) -> 신 -> 화면내용
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/piechart.fxml"));
			Scene scene = new Scene(root);
			Stage pieChartStage = new Stage(StageStyle.UTILITY);
			// +++++++++++++++++++++++이벤트등록 및 핸들러처리+++++++++++++++++++
			PieChart pieChart = (PieChart) scene.lookup("#pieChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// 두번클릭된 Student 객체를 가져오기
			Student student = obsList.get(tableViewSelectedIndex);

			// 파이차트에 입력할 내용을 observable list 입력한다.
			ObservableList pieChartList = FXCollections.observableArrayList();
			pieChartList.add(new PieChart.Data("국어", Integer.parseInt(student.getKorean())));
			pieChartList.add(new PieChart.Data("영어", Integer.parseInt(student.getEnglish())));
			pieChartList.add(new PieChart.Data("수학", Integer.parseInt(student.getMath())));
			pieChartList.add(new PieChart.Data("과학", Integer.parseInt(student.getSic())));
			pieChartList.add(new PieChart.Data("사회", Integer.parseInt(student.getSoc())));
			pieChartList.add(new PieChart.Data("음악", Integer.parseInt(student.getMusic())));

			pieChart.setData(pieChartList);

			btnClose.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					pieChartStage.close();
				}
			});
			// +++++++++++++++++++++++/이벤트등록 및 핸들러처리+++++++++++++++++++
			pieChartStage.initModality(Modality.WINDOW_MODAL);
			pieChartStage.initOwner(stage);
			pieChartStage.setScene(scene);
			pieChartStage.setResizable(false);
			pieChartStage.setTitle("성적 파이그래프");
			pieChartStage.show();
		} catch (IOException e) {
		}

	}

	// 바차트버튼이벤트등록 및 핸들러함수처리
	private void handleBtnBarChartAction(ActionEvent event) {
		// 내용 -> 신 -> 스테이지(스타일, 모달, 주인스테이지, 사이즈크기변경) -> 보여준다.
		try {
			if (obsList.size() == 0)
				throw new Exception();
			Parent root = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Scene scene = new Scene(root);
			Stage barChartStage = new Stage(StageStyle.UTILITY);
			// +++++++++++++++++++++++이벤트등록 및 핸들러처리+++++++++++++++++++
			BarChart barChart = (BarChart) scene.lookup("#barChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// 1. XYChart 시리즈를 만든다. (국어)
			XYChart.Series seriesKorean = new XYChart.Series();
			seriesKorean.setName("국어");
			ObservableList koreanList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				koreanList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesKorean.setData(koreanList);
			barChart.getData().add(seriesKorean);

			// 1. XYChart 시리즈를 만든다. (영어)
			XYChart.Series seriesEnglish = new XYChart.Series();
			seriesEnglish.setName("영어");
			ObservableList englishList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				englishList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getEnglish())));
			}
			seriesEnglish.setData(englishList);
			barChart.getData().add(seriesEnglish);

			// 1. XYChart 시리즈를 만든다. (수학)
			XYChart.Series seriesMath = new XYChart.Series();
			seriesMath.setName("수학");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				mathList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getMath())));
			}
			seriesMath.setData(mathList);
			barChart.getData().add(seriesMath);

			// 1. XYChart 시리즈를 만든다. (과학)
			XYChart.Series seriesSic = new XYChart.Series();
			seriesSic.setName("과학");
			ObservableList sicList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				sicList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getSic())));
			}
			seriesSic.setData(sicList);
			barChart.getData().add(seriesSic);

			// 1. XYChart 시리즈를 만든다. (사회)
			XYChart.Series seriesSoc = new XYChart.Series();
			seriesSoc.setName("사회");
			ObservableList socList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				socList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getSoc())));
			}
			seriesSoc.setData(socList);
			barChart.getData().add(seriesSoc);

			// 1. XYChart 시리즈를 만든다. (음악)
			XYChart.Series seriesMusic = new XYChart.Series();
			seriesMusic.setName("음악");
			ObservableList musicList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				musicList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getMusic())));
			}
			seriesMusic.setData(musicList);
			barChart.getData().add(seriesMusic);

			// +++++++++++++++++++++++/이벤트등록 및 핸들러처리+++++++++++++++++++
			barChartStage.initModality(Modality.WINDOW_MODAL);
			barChartStage.initOwner(stage);
			barChartStage.setScene(scene);
			barChartStage.setResizable(false);
			barChartStage.setTitle("성적 막대그래프");
			barChartStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("테이블뷰 리스트 입력요망!");
			alert.setHeaderText("데이타리스트를 입력하시오.");
			alert.setContentText("다음에는 주의하세요.");
			alert.showAndWait();
		}
	}

	// 점수입력창에 3자리수까지 입력셋팅(0~100)점수만 입력요망
	private void textFieldNumberFormat() {
		// 10진수 3자리까지만 입력툴을 제공하는 객체
		DecimalFormat decimalFormat = new DecimalFormat("###");

		txtKo.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtEng.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtMath.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtSic.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtSoc.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));

		txtMusic.setTextFormatter(new TextFormatter<>(e -> {
			// 1. 글자입력이 스페이스공백이면 다시 이벤트를 돌려준다.
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 2. 위치조사(키보드치는 위치추적해간다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 3. 숫자만 받겠다.(3글자만)
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE;
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("점수입력요망!");
				alert.setHeaderText("점수(0~100)를 입력하시오.");
				alert.setContentText("숫자외의 다른문자입력되지않은ㄴ다.");
				alert.showAndWait();
			}
			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null;
			} else {
				return e;
			}
		}));
	}

	// 테이블뷰UI객체 컬럼초기화셋팅(컬럼을 11개를 만들고 -> Student 클래스필드와 연결)
	private void tableViewColumnInitialize() {
		TableColumn colNo = new TableColumn("NO.");
		colNo.setMaxWidth(40);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));
		colNo.setStyle("-Fx-alignment: CENTER");

		TableColumn colName = new TableColumn("성명");
		colName.setMaxWidth(60);
		colName.setCellValueFactory(new PropertyValueFactory("name"));
		colName.setStyle("-Fx-alignment: CENTER");

		TableColumn colLevel = new TableColumn("학년");
		colLevel.setMaxWidth(50);
		colLevel.setCellValueFactory(new PropertyValueFactory("level"));
		colLevel.setStyle("-Fx-alignment: CENTER");

		TableColumn colBan = new TableColumn("반");
		colBan.setMaxWidth(40);
		colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));
		colBan.setStyle("-Fx-alignment: CENTER");

		TableColumn colGender = new TableColumn("성별");
		colGender.setMaxWidth(40);
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colGender.setStyle("-Fx-alignment: CENTER");

		TableColumn colKorean = new TableColumn("국어");
		colKorean.setMaxWidth(40);
		colKorean.setCellValueFactory(new PropertyValueFactory<>("korean"));
		colKorean.setStyle("-Fx-alignment: CENTER");

		TableColumn colEnglish = new TableColumn("영어");
		colEnglish.setMaxWidth(40);
		colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));
		colEnglish.setStyle("-Fx-alignment: CENTER");

		TableColumn colMath = new TableColumn("수학");
		colMath.setMaxWidth(40);
		colMath.setCellValueFactory(new PropertyValueFactory<>("math"));
		colMath.setStyle("-Fx-alignment: CENTER");

		TableColumn colSic = new TableColumn("과학");
		colSic.setMaxWidth(40);
		colSic.setCellValueFactory(new PropertyValueFactory<>("sic"));
		colSic.setStyle("-Fx-alignment: CENTER");

		TableColumn colSoc = new TableColumn("사회");
		colSoc.setMaxWidth(40);
		colSoc.setCellValueFactory(new PropertyValueFactory<>("soc"));
		colSoc.setStyle("-Fx-alignment: CENTER");

		TableColumn colMusic = new TableColumn("음악");
		colMusic.setMaxWidth(40);
		colMusic.setCellValueFactory(new PropertyValueFactory<>("music"));
		colMusic.setStyle("-Fx-alignment: CENTER");

		TableColumn colTotal = new TableColumn("총점");
		colTotal.setMaxWidth(50);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		colTotal.setStyle("-Fx-alignment: CENTER");

		TableColumn colAvg = new TableColumn("평균");
		colAvg.setMaxWidth(50);
		colAvg.setCellValueFactory(new PropertyValueFactory<>("avg"));
		colAvg.setStyle("-Fx-alignment: CENTER");

		TableColumn colRegister = new TableColumn("등록일");
		colRegister.setMaxWidth(110);
		colRegister.setCellValueFactory(new PropertyValueFactory<>("register"));

		TableColumn colFilename = new TableColumn("이미지");
		colFilename.setMaxWidth(400);
		colFilename.setCellValueFactory(new PropertyValueFactory<>("filename"));

		tableView.getColumns().addAll(colNo, colName, colLevel, colBan, colGender, colKorean, colEnglish, colMath,
				colSic, colSoc, colMusic, colTotal, colAvg, colRegister, colFilename);
		tableView.setItems(obsList);
	}

	// 총점버튼이벤트등록 및 핸들러함수처리
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
			// 경고창이 만들어짐(스테이지, 씬, 화면내용 싹 만들어짐)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("점수입력요망!");
			alert.setHeaderText("점수를 입력하시오.");
			alert.setContentText("다음에는 주의하세요.");
			alert.showAndWait();
		}
	}

	// 평균버튼이벤트등록 및 핸들러함수처리
	private void handleBtnAvgAction(ActionEvent event) {
		try {
			double avg = Integer.parseInt(txtTotal.getText()) / 6.0;
			txtAvg.setText(String.format("%.1f", avg));
		} catch (NumberFormatException e) {
			// 경고창이 만들어짐(스테이지, 씬, 화면내용 싹 만들어짐)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("점수입력요망!");
			alert.setHeaderText("총점이 입력이 안됐어요.");
			alert.setContentText("다음에는 주의하세요.");
			alert.showAndWait();
		}
	}

	// 초기화버튼이벤트등록 및 핸들러함수처리
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

	// 등록버튼이벤트등록 및 핸들러함수처리
	private void handleBtnOkAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// 이미지저장처리순서 1. 이미지파일명을 생성해서 복사해서 해당디렉토리에 저장한다.
		// 1. 이미지파일명을 만들어야한다.
		if (selectFile == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("사진선택없음!");
			alert.setHeaderText("사진가져오기 문제발생");
			alert.setContentText("이미지파일만 선택하세요!");
			alert.showAndWait();
			return;
		}
		// 2. 실제파이을 가져와서 새로만든 이미지 파일명에 저장한다.
		BufferedInputStream bis = null; // 파일을 읽을때 사용하는 클래스
		BufferedOutputStream bos = null; // 파일을 쓸때 사용하는 클래스
		String fileName = null;
		try {
			// "stu1238452179홍길동.jpg"
			fileName = "stu" + System.currentTimeMillis() + selectFile.getName();

			// 이미지파일은 바이트스트림으로 바꾸어서 버퍼를 활용해서 읽는다.
			// C:/images/stu1238452179홍길동.jpg
			bis = new BufferedInputStream(new FileInputStream(selectFile));
			bos = new BufferedOutputStream(new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
			// 이미지파일을 읽어서 저장위치에 있는 파일에다가 쓴다.
			// -1 : 더이상 읽을값이 없다는 의미이다.
			int data = -1;
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush(); // 버퍼에 있는값을 다 저장하기위해서 보내라.
			}
		} catch (Exception e) {
			System.out.println("파일복사에러" + e.getMessage());
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
			System.out.println("날짜를 입력해주세요.");
			return;
		}
		// 5. 어떤레코드를 지워야할지 해당된 쿼리문에 ? 번호를 연결한다.
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

	// 학년레벨을 입력하는 초기화 처리
	private void comboBoxLevelInitialize() {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		obsList.addAll("1학년", "2학년", "3학년", "4학년", "5학년", "6학년");
		cmdLevel.setItems(obsList);
	}

	// 성별라디오버튼 그룹 초기화 처리
	private void radioButtonGenderInitialize() {
		group = new ToggleGroup();
		rdoMale.setToggleGroup(group);
		rdoFemale.setToggleGroup(group);
		rdoMale.setSelected(true);
	}

	// 찾기버튼이벤트등록 및 핸들러함수처리
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
			// 경고창이 만들어짐(스테이지, 씬, 화면내용 싹 만들어짐)
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("등록입력문제발생!");
			alert.setHeaderText("Student 객체 점검을 해주세요.");
			alert.setContentText("다음에는 주의하세요.");
			alert.showAndWait();
		}
	}

	// 삭제버튼이벤트등록 및 핸들러함수처리
	private void handleBtnDeleteAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// 5. 어떤레코드를 지워야할지 해당된 쿼리문에 ? 번호를 연결한다
		Student student = obsList.get(tableViewSelectedIndex);
		int no = student.getNo();
		int returnValue = studentDAO.getStudentDelete(no);

		if (returnValue != 0) {
			//이미지 폴더에서 삭제대상이 된 사진을 삭제해야된다.
			//1. 삭제할 이미지 파일명을 가져온다.
			String filename = student.getFilename();
			File fileDelete = new File(directorySave.getAbsolutePath()+"\\"+filename);
			
			//2. 진짜로 파일이 있고, 정말 이미지 파일인지 확인한다.
			if(fileDelete.exists() && fileDelete.isFile()) {
				fileDelete.delete();
			}
			obsList.remove(tableViewSelectedIndex);
		}
	}

	// 테이블뷰를 선택을 했을때 이벤트등록 핸들러함수처리
	private void handleTableViewPressedAction(MouseEvent event) {
		tableViewSelectedIndex = tableView.getSelectionModel().getSelectedIndex();
	}

	// 수정버튼이벤트등록 및 핸들러함수처리
	private void handleBtnEditAction(ActionEvent event) {
		// formEdit.fxml 화면을 로드해야된다.
		try {
			// 화면내용->씬->스테이지(주인스테이지)->보여주면된다.을 가져왔어요.
			Parent root = FXMLLoader.load(getClass().getResource("/view/formEdit.fxml"));
			// scene(화면내용) 만든다.
			Scene scene = new Scene(root);
			Stage editStage = new Stage(StageStyle.UTILITY);
			// ++++++++++++++++++++++++이벤트등록 및 핸들러 처리+++++++++++++++++++++++
			// @FXML private TextField txtName -> 이것을 할수없다. (컨트롤러가 없기때문)
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

			// 테이블뷰에서 선택된 위치값을 가지고 observablelist에서 그 위치를 찾아서 해당된 student 객체를
			// 가져오면된다.
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
			// txtNo 텍스트필드를 read only(읽기만 가능) 만든다.(번호, 이름, 학년, 반, 성별)
			txtNo.setDisable(true);
			txtName.setDisable(true);
			txtYear.setDisable(true);
			txtBan.setDisable(true);
			txtGender.setDisable(true);
			// 계산버튼에 해당된 이벤트등록 및 핸들러 처리기능
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
			// 저장버튼 이벤트등록 및 핸들러 처리기능
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
						// 테이블뷰 obsList 해당된 위치에 수정된 객체값을 입력한다.
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

			// ++++++++++++++++++++++++/이벤트등록 및 핸들러 처리+++++++++++++++++++++++
			// 스테이지(주인스테이지)를 만든다. (*모달, 모달리스), 스테이지(씬)
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(stage);
			editStage.setScene(scene);
			editStage.setTitle("성적프로그램 수정창");
			editStage.setResizable(false);
			editStage.show();
		} catch (IndexOutOfBoundsException | IOException e) {
			// 경고창이 만들어짐(스테이지, 씬, 화면내용 싹 만들어짐)
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("수정창 에러!");
			alert.setHeaderText("점검요망");
			alert.setContentText("정신차리세요!");
			alert.showAndWait();
		}
	}

	// 데이터베이스(studentDB) 테이블(gradeTBL) 모든내용을 가져오기
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

	// 리스트버튼이벤트등록 및 핸들러함수처리
	private void handleBtnListAction(ActionEvent event) {
		obsList.clear();
		totalLoadList();
	}

	// 기본적인 이미지 사진 세팅하기
	private void setDefaultImageView() {
		Image image = new Image("/image/default.jpg", false);
		imgView.setImage(image);
	}

	// 이미지버튼이벤트등록 및 핸들러함수처리
	private void handleBtnImageFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		// 선택된 이미지 파일을 파일형식으로 돌려준다.
		selectFile = fileChooser.showOpenDialog(stage);
		selectFile.getName();
		try {
			if (selectFile != null) {
				// 사진의 실제경로가 문자열로 전환된다.
				String localURL = selectFile.toURI().toURL().toString();
				Image image = new Image(localURL, false);
				imgView.setImage(image);
			}
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("이미지버튼 에러!");
			alert.setHeaderText("이미지버튼 점검요망");
			alert.setContentText("이미지파일만 가져오시기 바랍니다.");
			alert.showAndWait();
		}
	}

	// 사진을 저장할수 있는 폴더 만들기 ("c:/images")
	private void setDirectoryImageSaveImage() {
		directorySave = new File("C:/images");
		if (!directorySave.exists()) {
			directorySave.mkdir();
			System.out.println("C:/images 디렉토리 이미지 만들기 성공!");
		}
	}

}
