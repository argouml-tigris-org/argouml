package org.argouml.util;

import java.io.*;
import java.util.zip.*;

public class SubInputStream extends java.io.FilterInputStream
{
    ZipInputStream in;
    public SubInputStream(ZipInputStream z)
    {
	super(z);
	in = z;
    } 
    public void close() throws IOException 
    {
	in.closeEntry();
    }

    public ZipEntry getNextEntry() throws IOException
    {
	return in.getNextEntry();
    }
}
