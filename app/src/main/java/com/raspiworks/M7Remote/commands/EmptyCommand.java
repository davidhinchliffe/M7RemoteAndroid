package com.raspiworks.M7Remote.commands;

/**
 * Concrete command to assign at initialization to avoid a null pointer
 * Author: David Hinchliffe <belgoi@gmail.com>
 */

public class EmptyCommand implements RemoteCommand {
    public void execute(){

    }
}
