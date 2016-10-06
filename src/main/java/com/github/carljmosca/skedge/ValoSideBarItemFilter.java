/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import com.github.carljmosca.skedge.view.Settings;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.components.AbstractSideBar.ItemFilter;

/**
 *
 * @author moscac
 */
@Component
public class ValoSideBarItemFilter implements ItemFilter {

    //TODO: modify to respect logged in user
    //TODO: look at SideBarItemDescriptor class for better way to do this
    
    @Override
    public boolean passesFilter(SideBarItemDescriptor descriptor) {
        
//        if ("Settings".equals(descriptor.getCaption()))
//            return false;
        return true;
    }
    
}
