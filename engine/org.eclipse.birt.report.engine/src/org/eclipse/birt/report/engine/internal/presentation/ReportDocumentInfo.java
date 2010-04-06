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

package org.eclipse.birt.report.engine.internal.presentation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * the report document information given out by the report document builder
 * 
 */
public class ReportDocumentInfo implements IReportDocumentInfo
{

	protected ExecutionContext context;
	protected long pageNumber;
	protected boolean finished;
	protected Map params = new HashMap( );
	protected Map parameterDisplayTexts = new HashMap( );
	protected Map beans = new HashMap( );
	protected List errors;

	public ReportDocumentInfo( ExecutionContext context, long pageNumber,
			boolean finished )
	{
		this.context = context;
		this.pageNumber = pageNumber;
		this.finished = finished;
		params.putAll( context.getParameterValues( ) );
		parameterDisplayTexts.putAll( context.getParameterDisplayTexts( ) );
		beans.putAll( context.getGlobalBeans( ) );
		errors = context.getErrors( );
	}

	public long getPageNumber( )
	{
		return pageNumber;
	}

	public boolean isComplete( )
	{
		return finished;
	}

	/**
	 * open the document for reading, the document must be closed by the caller.
	 * 
	 * @return
	 */
	public IReportDocument openReportDocument( ) throws BirtException
	{
		IReportEngine engine = context.getEngine( );
		ReportDocumentWriter writer = context.getReportDocWriter( );
		String documentName = writer.getName( );
		if ( new File( documentName ).isDirectory( ) )
		{
			char lastChar = documentName.charAt( documentName.length( ) - 1 );
			if ( lastChar != '\\' && lastChar != '/'
					&& lastChar != File.separatorChar )
			{
				documentName = documentName + File.separatorChar;
			}
		}
		if ( !finished )
		{
			IDocArchiveWriter arcWriter = writer.getArchive( );
			if ( arcWriter instanceof ArchiveWriter )
			{
				IArchiveFile archive = ( (ArchiveWriter) arcWriter )
						.getArchive( );
				try
				{
					IDocArchiveReader arcReader = new ArchiveReader( archive );
					IReportDocument document = engine.openReportDocument(
							documentName, arcReader, new HashMap( ) );
					return new TransientReportDocument( document, context,
							pageNumber, params, parameterDisplayTexts, beans,
							finished );
				}
				catch ( IOException ex )
				{
					return null;
				}
			}
		}
		IReportDocument document = engine.openReportDocument( documentName );

		return new TransientReportDocument( document, context, pageNumber,
				params, parameterDisplayTexts, beans, finished );
	}

	public List getErrors( )
	{
		return errors;
	}
}
