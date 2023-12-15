import getConnection.GetConnection;

import java.sql.Connection;

public class CheckFile {
    // check if file data exists in folder
    // if not, notify error to user
    // if exists, check connection to database control
    // if connection is ok, load data from file to database control
    // if connection is not ok, notify error to user
    Connection connControl;
   public void insertDataToData_config(){
       // insert data to data_config
       connControl = new GetConnection().getConnection("control");
   }


}
