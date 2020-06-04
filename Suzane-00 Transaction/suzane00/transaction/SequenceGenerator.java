/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import suzane00.global.Environment;

/**
 *
 * @author Usere
 */
public class SequenceGenerator {
    
    public static int getNextSequence(String _key) throws Exception  {
        try
        {
            Statement statement = Environment.getConnection().createStatement();
            int value = 1 ;
            if(statement.execute("SELECT get_next_sequence_number('" + _key +"')")) {
                ResultSet rs =  statement.getResultSet();
                if(!rs.next())
                    throw new Exception("Error in getting sequence value");
                
                value = rs.getInt(1);
            }
            
            return value ;
        }
        catch( SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public static String convertCalendarToSequenceString(Calendar _cal) {
        int year = _cal.get(Calendar.YEAR);
        int month = _cal.get(Calendar.MONTH);
        int day = _cal.get(Calendar.DAY_OF_MONTH);
        return "" + year + month + day;
    }
    
    public static String convertLocalDateToSequenceString(LocalDate _date) {
        int year = _date.getYear();
        int month = _date.getMonthValue();
        int day = _date.getDayOfMonth();
        return "" + year + month + day;
    }
    
}
