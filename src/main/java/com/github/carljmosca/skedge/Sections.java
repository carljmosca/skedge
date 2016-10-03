/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@SideBarSections({

        @SideBarSection(id = Sections.SUBMISSION, caption = "Submission")

})
@Component
public class Sections {


    public static final String SUBMISSION = "submission";
    
}
