/*
* File   : $Source: /alkacon/cvs/opencms/src/com/opencms/workplace/Attic/CmsAdminProjectLockchange.java,v $
* Date   : $Date: 2003/09/17 14:30:13 $
* Version: $Revision: 1.16 $
*
* This library is part of OpenCms -
* the Open Source Content Mananagement System
*
* Copyright (C) 2001  The OpenCms Group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* For further information about OpenCms, please see the
* OpenCms Website: http://www.opencms.org 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.opencms.workplace;

import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;

import com.opencms.core.CmsException;
import com.opencms.file.CmsObject;
import com.opencms.file.CmsProject;
import com.opencms.util.Utils;

import java.util.Hashtable;

/**
 * Template class for displaying OpenCms workplace admin project resent.
 * <P>
 * 
 * @author Andreas Schouten
 * @version $Revision: 1.16 $ $Date: 2003/09/17 14:30:13 $
 * @see com.opencms.workplace.CmsXmlWpTemplateFile
 */

public class CmsAdminProjectLockchange extends CmsWorkplaceDefault {
    
    /**
     * Gets the content of a defined section in a given template file and its subtemplates
     * with the given parameters. 
     * 
     * @see #getContent(CmsObject, String, String, Hashtable, String)
     * @param cms CmsObject Object for accessing system resources.
     * @param templateFile Filename of the template file.
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     */
    
    public byte[] getContent(CmsObject cms, String templateFile, String elementName, 
            Hashtable parameters, String templateSelector) throws CmsException {
        if(OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).isDebugEnabled() && C_DEBUG) {
            OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).debug("Getting content of element " + ((elementName==null)?"<root>":elementName));
            OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).debug("Template file is: " + templateFile);
            OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).debug("Selected template section is: " + ((templateSelector==null)?"<default>":templateSelector));
        }
        CmsXmlWpTemplateFile xmlTemplateDocument = (CmsXmlWpTemplateFile)getOwnTemplateFile(cms, 
                templateFile, elementName, parameters, templateSelector);
        int projectId = Integer.parseInt((String)parameters.get("projectid"));
        CmsProject project = cms.readProject(projectId);
        xmlTemplateDocument.setData("projectid", projectId + "");
        xmlTemplateDocument.setData("projectname", project.getName());
        if(parameters.get("ok") != null) {
            
            // change the lock of the project
            try {
                
                // unlock all resources in this project
                cms.unlockProject(projectId);
                
                // unlock project was successfull                
                // redirect to the project overview...
                templateSelector = "done";
            }
            catch(CmsException exc) {
                
                // error while lockchange
                if(OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).isWarnEnabled() ) {
                    OpenCms.getLog(CmsLog.CHANNEL_WORKPLACE_XML).warn("Error in admin project lock change", exc);
                }
                
                // get errorpage:
                xmlTemplateDocument.setData("details", Utils.getStackTrace(exc));
                templateSelector = "error";
            }
        }
        
        // Now load the template file and start the processing
        return startProcessing(cms, xmlTemplateDocument, elementName, parameters, 
                templateSelector);
    }
    
    /**
     * Indicates if the results of this class are cacheable.
     * 
     * @param cms CmsObject Object for accessing system resources
     * @param templateFile Filename of the template file 
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     * @return <EM>true</EM> if cacheable, <EM>false</EM> otherwise.
     */
    
    public boolean isCacheable(CmsObject cms, String templateFile, String elementName, 
            Hashtable parameters, String templateSelector) {
        return false;
    }
}
