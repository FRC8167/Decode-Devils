package org.firstinspires.ftc.teamcode.SubSystems;

public class Template_SingletonClass {

    /** Class Variable Go Here **/

    // Static variable to hold a single_instance of type Singleton.
    private static Template_SingletonClass single_instance = null;

    // Constructor
    // Here we will be creating private constructor restricted to this class itself
    private Template_SingletonClass() { }

    // Static method to create instance of Singleton class
    // Add any arguments required to create the object
    public static synchronized Template_SingletonClass getInstance()
    {
        if (single_instance == null)
            single_instance = new Template_SingletonClass();

        return single_instance;
    }

    /** Class Member Functions **/


}
