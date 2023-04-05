package helpdesk.model;

import java.sql.*;

public class RequestFormDAO {
    private static final String URL = "jdbc:mysql://localhost/helpdesk";
    private static final String[] prisijungimas = new String[]{"root", ""} ;

    public static void create(RequestForm requestForm) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO request_form (request_type, email, title, description, is_picture_included, area_of_interest) VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, requestForm.getRequestType());
        preparedStatement.setString(2, requestForm.getEmail());
        preparedStatement.setString(3, requestForm.getTitle());
        preparedStatement.setString(4, requestForm.getDescription());
        preparedStatement.setBoolean(5, requestForm.isPictureIncluded());
        preparedStatement.setString(6, requestForm.getAreaOfInterest());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void delete(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM request_form WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void update(RequestForm requestForm) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE request_form SET request_type = ?, email = ?, title = ?, description = ?, is_picture_included = ?, area_of_interest  = ? WHERE id = ?");
        preparedStatement.setString(1, requestForm.getRequestType());
        preparedStatement.setString(2, requestForm.getEmail());
        preparedStatement.setString(3, requestForm.getTitle());
        preparedStatement.setString(4, requestForm.getDescription());
        preparedStatement.setBoolean(5, requestForm.isPictureIncluded());
        preparedStatement.setString(6, requestForm.getAreaOfInterest());
        preparedStatement.setInt(7, requestForm.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static ResultSet search(String title) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        if (title.trim().length() < 1) {
            preparedStatement = connection.prepareStatement("SELECT * FROM request_form");
        } else {
            preparedStatement = connection.prepareStatement("SELECT * FROM request_form where title LIKE ?");
            preparedStatement.setString(1, String.format("%%%1$s%%", title.trim()));
        }
        return preparedStatement.executeQuery();
    }
}
