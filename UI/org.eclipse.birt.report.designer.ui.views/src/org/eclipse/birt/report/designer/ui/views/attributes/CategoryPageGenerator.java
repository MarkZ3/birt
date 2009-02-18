/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * CategoryPageGenerator provides default implementation of ICategoryProvider.
 */
public class CategoryPageGenerator extends TabPageGenerator
{

	public static final String ATTRIBUTESTITLE = Messages.getString( "PageGenerator.TabItem.Attributes" ); //$NON-NLS-1$
	public static final String BINDINGTITLE = Messages.getString( "PageGenerator.TabItem.Binding" ); //$NON-NLS-1$
	public static final String SORTINGTITLE = Messages.getString( "PageGenerator.TabItem.Sorting" ); //$NON-NLS-1$
	public static final String FILTERTITLE = Messages.getString( "PageGenerator.TabItem.Filters" ); //$NON-NLS-1$
	public static final String HIGHLIGHTSTITLE = Messages.getString( "PageGenerator.TabItem.Highlights" ); //$NON-NLS-1$
	public static final String GROUPSSTITLE = Messages.getString( "PageGenerator.TabItem.Groups" ); //$NON-NLS-1$
	public static final String MAPTITLE = Messages.getString( "PageGenerator.TabItem.map" ); //$NON-NLS-1$

	/**
	 * A <code>Composite<code> contains all category-like attribute pages.
	 */
	protected BaseAttributePage basicPage;

	private ICategoryProviderFactory factory = null;

	public CategoryPageGenerator( )
	{
		if ( factory == null )
		{
			factory = (ICategoryProviderFactory) Platform.getAdapterManager( )
					.getAdapter( this, ICategoryProviderFactory.class );
			if ( factory == null )
			{
				factory = CategoryProviderFactory.getInstance( );
			}
		}
	}

	public void refresh( )
	{
		// remove this?
		createTabItems( input );

		basicPage.setInput( input );
		addSelectionListener( this );
		basicPage.refresh( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.views.attributes.IPageGenerator#
	 * createTabItems(org.eclipse.swt.widgets.TabFolder, java.util.List)
	 */
	public void createTabItems( List input )
	{
		if ( basicPage == null || basicPage.getControl( ).isDisposed( ) )
		{
			super.createTabItems( input );
			tabFolder.setLayout( new FillLayout( ) );
			basicPage = new BaseAttributePage( );
			basicPage.buildUI( tabFolder );
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( ATTRIBUTESTITLE );
			tabItem.setControl( basicPage.getControl( ) );

			if ( customProvider != null )
				basicPage.setCategoryProvider( customProvider );
			else
				basicPage.setCategoryProvider( factory.getCategoryProvider( input ) );
		}
	}

	public void createControl( Composite parent, Object input )
	{
		super.createControl( parent, input );
	}

	private ICategoryProvider customProvider;

	public void setCategoryProvider( ICategoryProvider provider )
	{
		this.customProvider = provider;
	}

}