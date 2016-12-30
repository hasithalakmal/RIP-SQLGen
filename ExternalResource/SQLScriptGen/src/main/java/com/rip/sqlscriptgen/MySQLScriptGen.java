/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rip.sqlscriptgen;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Hasitha Lakmal
 */
public class MySQLScriptGen {

    HashMap dataTypes = new HashMap();

    public MySQLScriptGen() {
        dataTypes.put("rip_sql_short_string", "VARCHAR(10)");
        dataTypes.put("rip_sql_mediam_string", "VARCHAR(45)");
        dataTypes.put("rip_sql_long_string", "VARCHAR(1500)");
        dataTypes.put("rip_sql_small_lnteger", "SMALLINT");
        dataTypes.put("rip_sql_mediam_integer", "MEDIUMINT");
        dataTypes.put("rip_sql_big_integer", "BIGINT");
        dataTypes.put("rip_sql_float", "FLOAT(7,4)");
        dataTypes.put("rip_sql_date_with_tz", "DATETIME");
        dataTypes.put("rip_sql_date_without_tz", "DATETIME");
        dataTypes.put("rip_sql_date", "DATETIME");
        dataTypes.put("rip_sql_time", "DATETIME");
        dataTypes.put("rip_sql_timestamp", "TIMESTAMP");
        dataTypes.put("rip_sql_small_blob", "BLOB");
        dataTypes.put("rip_sql_mediam_blob", "MEDIUMBLOB");
        dataTypes.put("rip_sql_large_blob", "LONGBLOB");
        dataTypes.put("rip_sql_xml", "LONGTEXT");
        dataTypes.put("rip_sql_boolean", "TINYINT");

    }

    public String createDDLScript(JSONObject platformIndependentModel) {
        String msg = "";
        JSONObject Database_Design = platformIndependentModel.getJSONObject("Database_Design");
        JSONArray rip_sql_tables = Database_Design.getJSONArray("rip_sql_tables");

        for (int i = 0; i < rip_sql_tables.length(); i++) {
            JSONObject rip_sql_table = (JSONObject) rip_sql_tables.get(i);
            String table_script = this.createTable(rip_sql_table);
            msg = msg + table_script + "\n\n";
        }

        JSONArray rip_sql_forign_keys = Database_Design.getJSONArray("rip_sql_forign_keys");
        for (int i = 0; i < rip_sql_forign_keys.length(); i++) {
            JSONObject rip_sql_forign_key = (JSONObject) rip_sql_forign_keys.get(i);
            String fkry_script = this.foreignKeys(rip_sql_forign_key);
            msg = msg + fkry_script + "\n\n";
        }

        return msg;
    }

    public String createTable(JSONObject rip_sql_table) {
        String tble_script = "CREATE TABLE ";

        ArrayList pkey = new ArrayList();
        int pkey_counter = 0;

        ArrayList uniquekey = new ArrayList();
        int uniquekey_counter = 0;
        String rip_sql_table_name = rip_sql_table.getString("rip_sql_table_name");
        tble_script = tble_script + rip_sql_table_name + "(\n";
        JSONArray rip_sql_fileds = rip_sql_table.getJSONArray("rip_sql_fileds");
        for (int i = 0; i < rip_sql_fileds.length() - 1; i++) {
            JSONObject rip_sql_filed = (JSONObject) rip_sql_fileds.get(i);
            String rip_sql_feild_name = rip_sql_filed.getString("rip_sql_feild_name");

            String rip_sql_data_type = rip_sql_filed.getString("rip_sql_data_type");
            String dsm_datatype = (String) dataTypes.get(rip_sql_data_type);

            tble_script = tble_script + rip_sql_feild_name + " " + dsm_datatype;

            boolean rip_sql_primary_key = rip_sql_filed.getBoolean("rip_sql_primary_key");
            if (rip_sql_primary_key) {
                pkey.add(pkey_counter, rip_sql_feild_name);
                pkey_counter++;
            }
            boolean rip_sql_auto_incriment = rip_sql_filed.getBoolean("rip_sql_auto_incriment");
            if (rip_sql_auto_incriment) {
                tble_script = tble_script + " AUTO_INCREMENT ";
            }

            boolean rip_sql_not_null = rip_sql_filed.getBoolean("rip_sql_not_null");
            if (rip_sql_not_null) {
                tble_script = tble_script + " NOT NULL ";
            }

            boolean rip_sql_unique = rip_sql_filed.getBoolean("rip_sql_unique");
            if (rip_sql_unique) {
                uniquekey.add(uniquekey_counter, rip_sql_feild_name);
                uniquekey_counter++;
            }
            tble_script = tble_script + ",\n";
        }
        //loop pealing
        JSONObject rip_sql_filed = (JSONObject) rip_sql_fileds.get(rip_sql_fileds.length() - 1);
        String rip_sql_feild_name = rip_sql_filed.getString("rip_sql_feild_name");

        String rip_sql_data_type = rip_sql_filed.getString("rip_sql_data_type");
        String dsm_datatype = (String) dataTypes.get(rip_sql_data_type);

        tble_script = tble_script + rip_sql_feild_name + " " + dsm_datatype;

        boolean rip_sql_primary_key = rip_sql_filed.getBoolean("rip_sql_primary_key");
        if (rip_sql_primary_key) {
            pkey.add(pkey_counter, rip_sql_feild_name);
            pkey_counter++;
        }
        boolean rip_sql_auto_incriment = rip_sql_filed.getBoolean("rip_sql_auto_incriment");
        if (rip_sql_auto_incriment) {
            tble_script = tble_script + " AUTO_INCREMENT ";
        }

        boolean rip_sql_not_null = rip_sql_filed.getBoolean("rip_sql_not_null");
        if (rip_sql_not_null) {
            tble_script = tble_script + " NOT NULL ";
        }

        boolean rip_sql_unique = rip_sql_filed.getBoolean("rip_sql_unique");
        if (rip_sql_unique) {
            uniquekey.add(uniquekey_counter, rip_sql_feild_name);
            uniquekey_counter++;
        }

        if (pkey_counter > 0) {
            tble_script = tble_script + ",\n";
        }
        //primery key adding
        tble_script = tble_script + "PRIMARY KEY (";
        for (int i = 0; i < pkey_counter - 1; i++) {
            String pkeyFeild = (String) pkey.get(i);
            tble_script = tble_script + pkeyFeild + ",";
        }
        String pkeyFeild = (String) pkey.get(pkey_counter - 1);
        tble_script = tble_script + pkeyFeild + ")";
        if (uniquekey_counter > 0) {
            tble_script = tble_script + ",\n";
        }
        for (int i = 0; i < uniquekey_counter - 1; i++) {
            String uniquekeyFeild = (String) uniquekey.get(i);
            tble_script = tble_script + "UNIQUE INDEX " + uniquekeyFeild + "_UNIQUE ( " + uniquekeyFeild + " ASC),";
        }
        String uniquekeyFeild = (String) uniquekey.get(uniquekey_counter - 1);
        tble_script = tble_script + "UNIQUE INDEX " + uniquekeyFeild + "_UNIQUE ( " + uniquekeyFeild + " ASC)";
        tble_script = tble_script + "\n);";

        return tble_script;
    }

    public String foreignKeys(JSONObject rip_sql_forign_key) {
        String rip_sql_fk_name = rip_sql_forign_key.getString("rip_sql_fk_name");
        String rip_sql_base_table = rip_sql_forign_key.getString("rip_sql_base_table");
        String rip_sql_bt_feild_name = rip_sql_forign_key.getString("rip_sql_bt_feild_name");
        String rip_sql_reference_table = rip_sql_forign_key.getString("rip_sql_reference_table");
        String rip_sql_rt_feild_name = rip_sql_forign_key.getString("rip_sql_rt_feild_name");
        String rip_sql_on_delete = rip_sql_forign_key.getString("rip_sql_on_delete");
        String rip_sql_on_update = rip_sql_forign_key.getString("rip_sql_on_update");

        String foreignKey_script = "ALTER TABLE " + rip_sql_base_table + "\n"
                + "ADD CONSTRAINT " + rip_sql_fk_name + "\n"
                + "FOREIGN KEY ( " + rip_sql_bt_feild_name + ")\n"
                + "REFERENCES " + rip_sql_reference_table + "( " + rip_sql_rt_feild_name + " )"
                + "ON DELETE " + rip_sql_on_delete + "\n"
                + "ON UPDATE " + rip_sql_on_update + ";";
        ;

        return foreignKey_script;
    }

    public static void main(String[] args) {
        //create your class object name as databaseobject
        MySQLScriptGen databaseobject = new MySQLScriptGen();

        //this is same for every model
        JsonFileReader jsonFileReader = JsonFileReader.getInstance();
        JSONObject pim = jsonFileReader.getplatformIndependentModel();
        String script = databaseobject.createDDLScript(pim);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>RIP_SQL_GEN_BEGIN>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
        System.out.println(script);
        System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>RIP_SQL_GEN_END>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
