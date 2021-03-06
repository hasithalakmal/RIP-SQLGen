/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rip.sqlscriptgen;

import org.json.JSONObject;

/**
 *
 * @author Hasitha Lakmal
 */
public interface DatabasePulgginInterface {

    String createDDLScript(JSONObject platformIndependentModel);

    String createDMLScript(JSONObject platformIndependentModel);
    
    String createDQLScript(JSONObject platformIndependentModel);

    String createDeleteQuery(JSONObject rip_sql_table);

    String createInsertQuery(JSONObject rip_sql_table);
    
    String createSelectQuery(JSONObject rip_sql_table);

    String createTable(JSONObject rip_sql_table);

    String createUpdateQuery(JSONObject rip_sql_table);

    String foreignKeys(JSONObject rip_sql_forign_key);
    
}
