/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@RequestScoped
public class MenuController extends BaseController {
    public void error() {
        throw new NumberFormatException("This is an error!");
    }
}
