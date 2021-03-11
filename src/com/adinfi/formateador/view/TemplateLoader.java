/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adinfi.formateador.view;

import com.adinfi.formateador.bos.ModuleBO;
import com.adinfi.formateador.bos.TemplateBO;
import com.adinfi.formateador.bos.TemplateModuleBO;
import com.adinfi.formateador.dao.TemplateDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class TemplateLoader {

    /**
     * @param args the command line arguments
     */
    TemplateBO tempBO = new TemplateBO();
    ModuleBO module = new ModuleBO();
    TemplateModuleBO tempModBO = new TemplateModuleBO();
    List<TemplateModuleBO> lstModules = new ArrayList<>();
    TemplateDAO tempDAO = new TemplateDAO();

    public static void main(String[] args) {

        TemplateLoader loader = new TemplateLoader();

        for (int i = 19; i <= 19; i++) {
            loader.createTemplate(i);
        }

    }

    public void createTemplate(int TemplateNumber) {

        switch (TemplateNumber) {
            case 1:

                tempBO.setName("Plantilla 1");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(1);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(2);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);
                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());
                
                break;

            case 2:

                lstModules.clear();

                tempBO.setName("Plantilla 2");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(15);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 3:
                lstModules.clear();

                tempBO.setName("Plantilla 3");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(5);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 4:
                lstModules.clear();

                tempBO.setName("Plantilla 4B");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(6);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(2);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

//                module = new ModuleBO();
//                module.setModuleId(7);
//                module.setOrder((short) 3);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);
//
//                module = new ModuleBO();
//                module.setModuleId(8);
//                module.setOrder((short) 4);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 5:
                
                lstModules.clear();

                tempBO.setName("Plantilla 5");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(9);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(9);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 6:
                
                lstModules.clear();

                tempBO.setName("Plantilla 6");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(16);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

//                module = new ModuleBO();
//                module.setModuleId(5);
//                module.setOrder((short) 2);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);
//
//                module = new ModuleBO();
//                module.setModuleId(4);
//                module.setOrder((short) 3);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 7:
                
                lstModules.clear();

                tempBO.setName("Plantilla 7");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(5);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 8:
                
                lstModules.clear();

                tempBO.setName("Plantilla 8");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(4);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(4);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

//                module = new ModuleBO();
//                module.setModuleId(4);
//                module.setOrder((short) 3);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 9:
                
                lstModules.clear();

                tempBO.setName("Plantilla 9");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(10);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(10);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(10);
                module.setOrder((short) 3);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(10);
                module.setOrder((short) 4);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(11);
                module.setOrder((short) 5);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(11);
                module.setOrder((short) 6);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(11);
                module.setOrder((short) 7);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(11);
                module.setOrder((short) 8);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 10:
                
                lstModules.clear();

                tempBO.setName("Plantilla 10");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 734);

                module.setModuleId(5);
                module.setOrder((short) 1);

                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 11:
                
                lstModules.clear();

                tempBO.setName("Plantilla 11");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(1);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(2);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 12:
                
                lstModules.clear();

                tempBO.setName("Plantilla 12");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(17);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

//                module = new ModuleBO();
//                module.setModuleId(1);
//                module.setOrder((short) 2);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);
//
//                module = new ModuleBO();
//                module.setModuleId(1);
//                module.setOrder((short) 3);
//                tempModBO = new TemplateModuleBO();
//                tempModBO.setModule(module);
//                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 13:
                
                lstModules.clear();

                tempBO.setName("Plantilla 13");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(3);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(4);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(7);
                module.setOrder((short) 3);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 14:
                
                lstModules.clear();

                tempBO.setName("Plantilla 14");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(6);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(5);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 15:
                
                lstModules.clear();

                tempBO.setName("Plantilla 15");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(2);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(6);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(2);
                module.setOrder((short) 3);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(6);
                module.setOrder((short) 4);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 16:
                
                lstModules.clear();

                tempBO.setName("Plantilla 16");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(12);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;
            case 17:
                
                lstModules.clear();

                tempBO.setName("Plantilla 17");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(13);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 18:
                
                lstModules.clear();

                tempBO.setName("Plantilla 18");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(18);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

            case 19:
                
                lstModules.clear();

                tempBO.setName("Plantilla 19");
                tempBO.setWidth((short) 580);
                tempBO.setHeight((short) 726);

                module.setModuleId(2);
                module.setOrder((short) 1);
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                module = new ModuleBO();
                module.setModuleId(6);
                module.setOrder((short) 2);
                tempModBO = new TemplateModuleBO();
                tempModBO.setModule(module);
                lstModules.add(tempModBO);

                tempBO.setModulesAsList(lstModules);

                tempDAO.addTemplate(tempBO);
                System.out.println(tempBO.getTemplateId());

                break;

        }

    }

}
