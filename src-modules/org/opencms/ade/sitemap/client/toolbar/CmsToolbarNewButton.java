/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/sitemap/client/toolbar/Attic/CmsToolbarNewButton.java,v $
 * Date   : $Date: 2011/02/10 16:35:54 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ade.sitemap.client.toolbar;

import org.opencms.ade.sitemap.client.CmsSitemapView;
import org.opencms.ade.sitemap.client.Messages;
import org.opencms.ade.sitemap.client.control.CmsSitemapController;
import org.opencms.ade.sitemap.client.ui.CmsCreatableListItem;
import org.opencms.ade.sitemap.client.ui.CmsCreatableListItem.EntryType;
import org.opencms.ade.sitemap.shared.CmsNewResourceInfo;
import org.opencms.gwt.client.ui.CmsList;
import org.opencms.gwt.client.ui.CmsListItem;
import org.opencms.gwt.client.ui.CmsListItemWidget;
import org.opencms.gwt.client.ui.I_CmsButton;
import org.opencms.gwt.client.ui.I_CmsListItem;
import org.opencms.gwt.shared.CmsListInfoBean;

/**
 * Sitemap toolbar new menu button.<p>
 * 
 * @author Tobias Herrmann
 * 
 * @version $Revision: 1.1 $
 * 
 * @since 8.0.0
 */
public class CmsToolbarNewButton extends A_CmsToolbarListMenuButton {

    /** The tag for identifying redirect items. */
    public static final String TAG_REDIRECT = "redirect";

    /** The tag for identifying items from the special tab. */
    public static final String TAG_SPECIAL = "special";

    /** The new-elements list. */
    private CmsList<I_CmsListItem> m_newElementsList;

    /** The special elements list. */
    private CmsList<I_CmsListItem> m_specialList;

    /**
     * Constructor.<p>
     * 
     * @param toolbar the toolbar instance
     * @param controller the sitemap controller 
     */
    public CmsToolbarNewButton(CmsSitemapToolbar toolbar, CmsSitemapController controller) {

        super("New", I_CmsButton.ButtonData.ADD.getIconClass(), toolbar, controller);
    }

    /**
     * @see org.opencms.ade.sitemap.client.toolbar.A_CmsToolbarListMenuButton#initContent()
     */
    @Override
    protected void initContent() {

        m_newElementsList = new CmsList<I_CmsListItem>();
        for (CmsNewResourceInfo info : getController().getData().getNewElementInfos()) {
            m_newElementsList.add(makeNewElementItem(info));
        }
        createTab("New pages", "Create a new page", m_newElementsList);
        m_specialList = new CmsList<I_CmsListItem>();
        // TODO: add redirect item
        //       m_specialList.add(makeRedirectItem());
        if (CmsSitemapView.getInstance().getController().getData().canEditDetailPages()) {
            for (CmsNewResourceInfo typeInfo : CmsSitemapView.getInstance().getController().getData().getResourceTypeInfos()) {
                CmsCreatableListItem item = makeDetailPageItem(typeInfo);
                m_specialList.add(item);
            }
        }
        createTab(Messages.get().key(Messages.GUI_SPECIAL_TAB_TITLE_0), "The special tab", m_specialList);
    }

    /**
     * Creates a list item representing a detail page to be created.<p>
     * 
     * @param typeInfo the bean for the type for which the detail page item should be created
     *  
     * @return the detail page list item  
     */
    private CmsCreatableListItem makeDetailPageItem(CmsNewResourceInfo typeInfo) {

        CmsListInfoBean info = new CmsListInfoBean();
        String subtitle = typeInfo.getTypeName();
        String title = "Detail page for [" + typeInfo.getTitle() + "]";
        info.setTitle(title);
        info.setSubTitle(subtitle);
        CmsListItemWidget widget = new CmsListItemWidget(info);
        widget.setIcon(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.listItemWidgetCss().normal());
        CmsCreatableListItem listItem = new CmsCreatableListItem(widget, typeInfo, EntryType.detailpage);
        listItem.addTag(TAG_SPECIAL);
        listItem.initMoveHandle(CmsSitemapView.getInstance().getTree().getDnDHandler());
        return listItem;
    }

    /**
     * Create a new-element list item.<p>
     * 
     * @param typeInfo the new-element info
     * 
     * @return the list item
     */
    private CmsCreatableListItem makeNewElementItem(CmsNewResourceInfo typeInfo) {

        CmsListInfoBean info = new CmsListInfoBean();
        info.setTitle(typeInfo.getTitle());
        info.setSubTitle(typeInfo.getTypeName());
        if ((typeInfo.getDescription() != null) && (typeInfo.getDescription().trim().length() > 0)) {
            info.addAdditionalInfo("Description", typeInfo.getDescription());
        }
        CmsListItemWidget widget = new CmsListItemWidget(info);
        widget.setIcon(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.listItemWidgetCss().normal());
        CmsCreatableListItem listItem = new CmsCreatableListItem(widget, typeInfo, EntryType.regular);
        listItem.initMoveHandle(CmsSitemapView.getInstance().getTree().getDnDHandler());
        return listItem;
    }

    /**
     * Creates a list item representing a redirect.<p>
     * 
     * @return the new list item 
     */
    private CmsListItem makeRedirectItem() {

        CmsListInfoBean info = new CmsListInfoBean();
        String title = Messages.get().key(Messages.GUI_REDIRECT_TITLE_0);
        String subtitle = Messages.get().key(Messages.GUI_REDIRECT_SUBTITLE_0);
        info.setTitle(title);
        info.setSubTitle(subtitle);
        CmsListItemWidget widget = new CmsListItemWidget(info);
        widget.setIcon(org.opencms.gwt.client.ui.css.I_CmsLayoutBundle.INSTANCE.listItemWidgetCss().redirect());
        CmsListItem listItem = new CmsListItem(widget);
        listItem.addTag(TAG_REDIRECT);
        listItem.addTag(TAG_SPECIAL);

        listItem.initMoveHandle(CmsSitemapView.getInstance().getTree().getDnDHandler());
        return listItem;
    }
}