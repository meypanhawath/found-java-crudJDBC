import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.Scanner;

public class CrudOperation {

    // final mean cannot override
    private static final String url = "jdbc:postgresql://localhost:5432/test_db";
    private static final String user = "postgres";
    private static final String password = "Raz@0205";

    private static final Scanner input = new Scanner(System.in);

    public static boolean existByID(int id) throws SQLException{
        Connection con = DriverManager.getConnection(url, user, password);

        String sql = """
                SELECT 1 FROM users
                WHERE id = ?
                """;
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    public void createUser() throws SQLException {

        Connection con  = DriverManager.getConnection(url, user, password);
        System.out.print("Enter user_id: ");
        int id = Integer.parseInt(input.nextLine());
        System.out.print("Enter user_name: ");
        String name = input.nextLine();
        System.out.print("Enter user_age: ");
        int age = Integer.parseInt(input.nextLine());

        User user = new User(id, name, age);
        String sql = """
                INSERT INTO users
                values (?, ?, ?)
                """;

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, user.getId());
        ps.setString(2, user.getName());
        ps.setInt(3, user.getAge());

        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) System.out.println("ការបញ្ចូលទិន្នន៍យ ជោគជ៍យ");
        else System.out.println("ការបញ្ជូលទិន្នន៍យ បរាជ៍យ");
    }

    public void readUserByID() throws SQLException{
        Connection con = DriverManager.getConnection(url, user, password);

        System.out.print("Search User by ID: ");
        int id = Integer.parseInt(input.nextLine());

        if (!existByID(id)) {
            System.out.println("User does not exist!");
            return;
        }

        String sql = """
                SELECT * FROM users
                WHERE id = ?
                """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );
            System.out.println("=".repeat(30));
            System.out.println(user);
            System.out.println("=".repeat(30));
        }
    }

    public void updateUserByID() throws SQLException{
        Connection con = DriverManager.getConnection(url, user, password);

        System.out.print("Update User by ID: ");
        int id = Integer.parseInt(input.nextLine());

        if(!existByID(id)){
            System.out.println("User does not exist!");
            return;
        }

        System.out.print("Enter new name: ");
        String name = input.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(input.nextLine());

        String sql = """
                UPDATE users
                SET name = ?, age = ?
                WHERE id = ?
                """;
        User user = new User(id, name, age);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, age);
        ps.setInt(3, id);

        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) System.out.println("ការបញ្ចូលទិន្នន៍យ ជោគជ៍យ");
        else System.out.println("ការបញ្ជូលទិន្នន៍យ បរាជ៍យ");

    }

    public void deleteUserByID() throws  SQLException {
        Connection con =
    }

    public void displayAllUser() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);

        System.out.println("Display All Users");

        String sql = """
                SELECT * FROM users
                """;

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        Table table = new Table(
                3,
                BorderStyle.UNICODE_BOX_DOUBLE_BORDER
        );
        table.addCell(" ID   ");
        table.addCell(" Name     ");
        table.addCell(" Age   ");

        while (rs.next()){
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            Integer age = rs.getInt("age");

//            System.out.printf(" ID=%d, Name=%s, Age=%d\n",
//                    id, name, age);
            table.addCell(String.valueOf(id));
            table.addCell(name);
            table.addCell(String.valueOf(age));
        }

        System.out.println(table.render());

    }

    public static void main(String[] args){
        CrudOperation crudOperation = new CrudOperation();

        while(true){
            System.out.println("""
                === Main Menu ===
                1, Add User
                2, Search User
                3, Update User
                4, Delete User
                5, View All User
                0, Exit
                """);
            System.out.print("Choose option: ");
            int opt = Integer.parseInt(input.nextLine());

            if (opt == 0) break;

            try{
                switch (opt){
                    case 1 -> crudOperation.createUser();
                    case 2 -> crudOperation.readUserByID();
                    case 3 -> crudOperation.updateUserByID();
                    case 4 -> crudOperation.deleteUserByID();
                    case 5 -> crudOperation.displayAllUser();
                    default -> System.out.println("Invalid option. Choose[1-5] or [0] to exit.");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }



    }
}
