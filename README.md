# rcp-update
This repo is to show how to work with RCP automatic update.

#1. Plugin Project
1. Create a plugin project called **xdemo**

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763224965_34.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763249332_18.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763292688_88.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476763305103_57.png)

1. Add p2 plugins to the dependencies
  - org.eclipse.equinox.p2.core
  - org.eclipse.equinox.p2.engine
  - org.eclipse.equinox.p2.operations
  - org.eclipse.equinox.p2.metadata.repository

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476764022214_10.png)

1. Add automatic update function to this plugin
  1. Create a Model class
  1. Create **UpdateHandler.class** with following content, this is the handler for automatic update
  1. Modify UI code

Add an update command

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765113081_12.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765139744_30.png)

Add an update handler

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765158120_17.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765196343_27.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765216168_99.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765250799_31.png)

Add an update menu

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765278824_72.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765310711_25.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765324464_43.png)

1. After all changes, we run our rcp application

We now have to change run configure as following

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765523543_41.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765539198_96.png)

Now we should be able to see the UI, and we could save the text and press **Update** Menu. However, the update function won't, you should see this in eclipse console - *Trying to update from the Eclipse IDE? This won't work!*

![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765563940_23.png)
![alt text](https://github.com/Sean-PAN2014/rcp-update/blob/master/pic/1.%20Plugin%20Project/panxinyang_1476765586020_16.png)

#2. Create Feature Project

#3. Create Product Project
