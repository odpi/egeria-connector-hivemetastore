/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;


public class MockColumn {

   private String name = null;
   private String type = null;
   public MockColumn(String name , String type) {
       this.name =name;
       this.type = type;
   }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
