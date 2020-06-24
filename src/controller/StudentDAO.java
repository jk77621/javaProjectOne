package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Student;

public class StudentDAO {
	// 데이터베이스(studentDB) 테이블(gradeTBL) 모든내용을 가져오기
	public ArrayList<Student> getTotalLoadList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB 연결성공!");
			} else {
				System.out.println("RootController.totalLoadList : DB 연결실패!");
			}
			// 3. con 객체를 가지고 쿼리문을 실행할수있다. (select, insert, update, delete)
			String query = "select * from gradeTBL";
			// 4. 쿼리문을 실행하기위한 준비
			pstmt = con.prepareStatement(query);
			// 5. 쿼리문을 싱행한다. (결과값의 레코드내용을 배열로 가져온다.)
			rs = pstmt.executeQuery();
			// 6. ResultSet 값을 한개씩 가져와서 ArrayList에 저장한다.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
								rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
				arrayList.add(student);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("TotalList 점검요망!");
			alert.setHeaderText("TotalList 문제발생!");
			alert.setContentText("문제사항 : " + e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println("RootController.totalLoadList : " + e.getMessage());
			}
		}
		return arrayList;
	}
	// 이름을 통해서 찾기 기능을 해서 해당된 레코드를 가져오기
	public ArrayList<Student> getStudentFind(String name) {
		// 데이터베이스를 실행합니다.=================================================
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB 연결성공!");
			} else {
				System.out.println("RootController.totalLoadList : DB 연결실패!");
			}
			// 3. con 객체를 가지고 쿼리문을 실행할수있다. (select, insert, update, delete)
			String query = "select * from gradeTBL where name like ?";
			// 4. 쿼리문을 실행하기위한 준비
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// 5. 쿼리문을 싱행한다. (결과값의 레코드내용을 배열로 가져온다.)
			rs = pstmt.executeQuery();
			// 6. ResultSet 값을 한개씩 가져와서 ArrayList에 저장한다.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
				arrayList.add(student);
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("검색 점검요망!");
			alert.setHeaderText("검색 문제발생!");
			alert.setContentText("문제사항 : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.out.println("RootController.handleBtnSearchAction : " + e2.getMessage());
			}
		}
		// 데이터 베이스 종점======================================================
		return arrayList;
	}
	// 입력하기
	public int getStudentRegistry(Student student) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnOkAction : DB 연결 성공");
			} else
				System.out.println("RootController.BtnOkAction : DB 연결 실패");

			// 3. con 객체를 가지고 쿼리문 실행할수있다(select,insert,update,delete)
			String query = "insert into gradeTBL values(Null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			// 4. 쿼리문 실행하기 위한 준비
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, student.getName());
			pstmt.setString(2, student.getLevel());
			pstmt.setString(3, student.getBan());
			pstmt.setString(4, student.getGender());
			pstmt.setString(5, student.getKorean());
			pstmt.setString(6, student.getEnglish());
			pstmt.setString(7, student.getMath());
			pstmt.setString(8, student.getSic());
			pstmt.setString(9, student.getSoc());
			pstmt.setString(10, student.getMusic());
			pstmt.setString(11, student.getTotal());
			pstmt.setString(12, student.getAvg());
			pstmt.setString(13, student.getRegister());
			pstmt.setString(14, student.getFilename());

			// 5. 쿼리문을 실행한다
			// 쿼리문을 실행해서 레코드내용의 결과값을 배열로 가져온다 : executeQuery();
			// 또는 단지 실행만 한다 : executeUqdate();
			returnValue = pstmt.executeUpdate();// 아무것도안하면 0 리턴, 실행한 카운트만큼 int값 반환
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("등록 성공");
				alert.setHeaderText(student.getName() + "번 등록 성공");
				alert.setContentText(student.getName() + " 하이하이");
				alert.showAndWait();
			} else {
				throw new Exception("등록에 문제있음");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("등록 점검요망");
			alert.setHeaderText("등록문제발생!\n");
			alert.setContentText("문제사항 : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println("RootController.BtnOkAction : " + e1.getMessage());
			}
		}
		return returnValue;
	}
	// 삭제하기
	public int getStudentDelete(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB 연결 성공");
			} else
				System.out.println("RootController.BtnDeleteAction : DB 연결 실패");

			// 3. con 객체를 가지고 쿼리문 실행할수있다(select,insert,update,delete)
			String query = "delete from gradeTBL where no = ?";
			// 4. 쿼리문 실행하기 위한 준비
			pstmt = con.prepareStatement(query); // 외워라
			pstmt.setInt(1, no); // 1의 의미:첫번째 ?, ?가 두개 2로하면 두번째 ?에 들어간다
			// 5. 쿼리문을 실행한다
			// 쿼리문을 실행해서 레코드내용의 결과값을 배열로 가져온다 : executeQuery();
			// 또는 단지 실행만 한다 : executeUqdate();
			returnValue = pstmt.executeUpdate();// 아무것도안하면 0 리턴, 실행한 카운트만큼 int값 반환
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("삭제 성공");
				alert.setHeaderText(no + "번 삭제 성공");
				alert.setContentText(no + "번님 안녕히가세요.");
				alert.showAndWait();
			} else {
				throw new Exception("삭제에 문제있음");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("삭제 점검요망 점검요망");
			alert.setHeaderText("삭제 문제발생!\n");
			alert.setContentText("문제사항 : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println("RootController.BtnDeleteAction : " + e1.getMessage());
			}
		}
		return returnValue;
	}
	// 수정하기
	public int getStudentUpdate(Student stu) {
		// 데이터베이스 작업을 진행한다.
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB 연결 성공");
			} else
				System.out.println("RootController.BtnDeleteAction : DB 연결 실패");

			// 3. con 객체를 가지고 쿼리문 실행할수있다(select,insert,update,delete)
			String query = "update gradeTBL set korean=?, english=?, math=?, sic=?,  "
					+ " soc=?, music=?, total=?, average=? where no=?";
			// 4. 쿼리문 실행하기 위한 준비
			pstmt = con.prepareStatement(query); // 외워라
			// 5. 어떤레코드를 지워야할지 해당된 쿼리문에 ? 번호를 연결한다
			pstmt.setString(1, stu.getKorean()); // 1의 의미:첫번째 ?, ?가 두개 2로하면 두번째 ?에 들어간다
			pstmt.setString(2, stu.getEnglish());
			pstmt.setString(3, stu.getMath());
			pstmt.setString(4, stu.getSic());
			pstmt.setString(5, stu.getSoc());
			pstmt.setString(6, stu.getMusic());
			pstmt.setString(7, stu.getTotal());
			pstmt.setString(8, stu.getAvg());
			pstmt.setInt(9, stu.getNo());
			// 5. 쿼리문을 실행한다
			// 쿼리문을 실행해서 레코드내용의 결과값을 배열로 가져온다 : executeQuery();
			// 또는 단지 실행만 한다 : executeUqdate();
			returnValue = pstmt.executeUpdate();// 아무것도안하면 0 리턴, 실행한 카운트만큼 int값 반환
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("수정 성공");
				alert.setHeaderText(stu.getNo() + "번 수정 성공");
				alert.setContentText(stu.getName() + "님 수정했습니다.");
				alert.showAndWait();
			} else {
				throw new Exception("수정에 문제있음");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("수정 점검요망 점검요망");
			alert.setHeaderText("수정 문제발생!\n RootController.handleBtnEditAction");
			alert.setContentText("RootController.handleBtnEditAction : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt 반납
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.out.println("RootController.BtnEditAction : " + e2.getMessage());
			}
		}
		return returnValue;
	}
}
