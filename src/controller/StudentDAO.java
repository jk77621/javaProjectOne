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
	// �����ͺ��̽�(studentDB) ���̺�(gradeTBL) ��系���� ��������
	public ArrayList<Student> getTotalLoadList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB ���Ἲ��!");
			} else {
				System.out.println("RootController.totalLoadList : DB �������!");
			}
			// 3. con ��ü�� ������ �������� �����Ҽ��ִ�. (select, insert, update, delete)
			String query = "select * from gradeTBL";
			// 4. �������� �����ϱ����� �غ�
			pstmt = con.prepareStatement(query);
			// 5. �������� �����Ѵ�. (������� ���ڵ峻���� �迭�� �����´�.)
			rs = pstmt.executeQuery();
			// 6. ResultSet ���� �Ѱ��� �����ͼ� ArrayList�� �����Ѵ�.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), 
								rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
				arrayList.add(student);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("TotalList ���˿��!");
			alert.setHeaderText("TotalList �����߻�!");
			alert.setContentText("�������� : " + e.getMessage());
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
	// �̸��� ���ؼ� ã�� ����� �ؼ� �ش�� ���ڵ带 ��������
	public ArrayList<Student> getStudentFind(String name) {
		// �����ͺ��̽��� �����մϴ�.=================================================
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Student> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.totalLoadList : DB ���Ἲ��!");
			} else {
				System.out.println("RootController.totalLoadList : DB �������!");
			}
			// 3. con ��ü�� ������ �������� �����Ҽ��ִ�. (select, insert, update, delete)
			String query = "select * from gradeTBL where name like ?";
			// 4. �������� �����ϱ����� �غ�
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// 5. �������� �����Ѵ�. (������� ���ڵ峻���� �迭�� �����´�.)
			rs = pstmt.executeQuery();
			// 6. ResultSet ���� �Ѱ��� �����ͼ� ArrayList�� �����Ѵ�.
			arrayList = new ArrayList<Student>();
			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
				arrayList.add(student);
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�˻� ���˿��!");
			alert.setHeaderText("�˻� �����߻�!");
			alert.setContentText("�������� : " + e1.getMessage());
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
		// ������ ���̽� ����======================================================
		return arrayList;
	}
	// �Է��ϱ�
	public int getStudentRegistry(Student student) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnOkAction : DB ���� ����");
			} else
				System.out.println("RootController.BtnOkAction : DB ���� ����");

			// 3. con ��ü�� ������ ������ �����Ҽ��ִ�(select,insert,update,delete)
			String query = "insert into gradeTBL values(Null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			// 4. ������ �����ϱ� ���� �غ�
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

			// 5. �������� �����Ѵ�
			// �������� �����ؼ� ���ڵ峻���� ������� �迭�� �����´� : executeQuery();
			// �Ǵ� ���� ���ุ �Ѵ� : executeUqdate();
			returnValue = pstmt.executeUpdate();// �ƹ��͵����ϸ� 0 ����, ������ ī��Ʈ��ŭ int�� ��ȯ
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��� ����");
				alert.setHeaderText(student.getName() + "�� ��� ����");
				alert.setContentText(student.getName() + " ��������");
				alert.showAndWait();
			} else {
				throw new Exception("��Ͽ� ��������");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("��� ���˿��");
			alert.setHeaderText("��Ϲ����߻�!\n");
			alert.setContentText("�������� : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt �ݳ�
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
	// �����ϱ�
	public int getStudentDelete(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");
			} else
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");

			// 3. con ��ü�� ������ ������ �����Ҽ��ִ�(select,insert,update,delete)
			String query = "delete from gradeTBL where no = ?";
			// 4. ������ �����ϱ� ���� �غ�
			pstmt = con.prepareStatement(query); // �ܿ���
			pstmt.setInt(1, no); // 1�� �ǹ�:ù��° ?, ?�� �ΰ� 2���ϸ� �ι�° ?�� ����
			// 5. �������� �����Ѵ�
			// �������� �����ؼ� ���ڵ峻���� ������� �迭�� �����´� : executeQuery();
			// �Ǵ� ���� ���ุ �Ѵ� : executeUqdate();
			returnValue = pstmt.executeUpdate();// �ƹ��͵����ϸ� 0 ����, ������ ī��Ʈ��ŭ int�� ��ȯ
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("���� ����");
				alert.setHeaderText(no + "�� ���� ����");
				alert.setContentText(no + "���� �ȳ���������.");
				alert.showAndWait();
			} else {
				throw new Exception("������ ��������");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("���� ���˿�� ���˿��");
			alert.setHeaderText("���� �����߻�!\n");
			alert.setContentText("�������� : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt �ݳ�
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
	// �����ϱ�
	public int getStudentUpdate(Student stu) {
		// �����ͺ��̽� �۾��� �����Ѵ�.
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");
			} else
				System.out.println("RootController.BtnDeleteAction : DB ���� ����");

			// 3. con ��ü�� ������ ������ �����Ҽ��ִ�(select,insert,update,delete)
			String query = "update gradeTBL set korean=?, english=?, math=?, sic=?,  "
					+ " soc=?, music=?, total=?, average=? where no=?";
			// 4. ������ �����ϱ� ���� �غ�
			pstmt = con.prepareStatement(query); // �ܿ���
			// 5. ����ڵ带 ���������� �ش�� �������� ? ��ȣ�� �����Ѵ�
			pstmt.setString(1, stu.getKorean()); // 1�� �ǹ�:ù��° ?, ?�� �ΰ� 2���ϸ� �ι�° ?�� ����
			pstmt.setString(2, stu.getEnglish());
			pstmt.setString(3, stu.getMath());
			pstmt.setString(4, stu.getSic());
			pstmt.setString(5, stu.getSoc());
			pstmt.setString(6, stu.getMusic());
			pstmt.setString(7, stu.getTotal());
			pstmt.setString(8, stu.getAvg());
			pstmt.setInt(9, stu.getNo());
			// 5. �������� �����Ѵ�
			// �������� �����ؼ� ���ڵ峻���� ������� �迭�� �����´� : executeQuery();
			// �Ǵ� ���� ���ุ �Ѵ� : executeUqdate();
			returnValue = pstmt.executeUpdate();// �ƹ��͵����ϸ� 0 ����, ������ ī��Ʈ��ŭ int�� ��ȯ
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("���� ����");
				alert.setHeaderText(stu.getNo() + "�� ���� ����");
				alert.setContentText(stu.getName() + "�� �����߽��ϴ�.");
				alert.showAndWait();
			} else {
				throw new Exception("������ ��������");
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("���� ���˿�� ���˿��");
			alert.setHeaderText("���� �����߻�!\n RootController.handleBtnEditAction");
			alert.setContentText("RootController.handleBtnEditAction : " + e1.getMessage());
			alert.showAndWait();
		} finally {
			// con,pstmt �ݳ�
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
