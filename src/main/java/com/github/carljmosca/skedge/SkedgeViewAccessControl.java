/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;

@SpringComponent
public class SkedgeViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String viewName) {
        
//        if (ValoSideBarUI.get().email == null || ValoSideBarUI.get().email.isEmpty()) {
//            return false;
//        }

        return true;
    }

    
}
