package net.youmi.ads.base.utils;

import android.database.Cursor;

import net.youmi.ads.base.log.DLog;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhitao
 * @since 2017-04-13 15:22
 */
public class IOUtils {
	
	public static void close(Object... objs) {
		for (Object obj : objs) {
			close(obj);
		}
	}
	
	public static void closeOS(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeIS(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeReader(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeWriter(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeFile(RandomAccessFile file) {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeSocket(Socket socket) {
		if (socket != null) {
			if (socket.isConnected()) {
				try {
					socket.close();
				} catch (IOException e) {
					DLog.e(e);
				}
			}
		}
	}
	
	public static void closeServerSocket(ServerSocket socket) {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	public static void closeProcess(Process process) {
		if (process != null) {
			process.destroy();
		}
	}
	
	public static void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
	
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				DLog.e(e);
			}
		}
	}
	
	private static void close(Object obj) {
		if (obj == null) {
			return;
		}
		
		if (obj instanceof InputStream) {
			closeIS((InputStream) obj);
		} else if (obj instanceof OutputStream) {
			closeOS((OutputStream) obj);
		} else if (obj instanceof Writer) {
			closeWriter((Writer) obj);
		} else if (obj instanceof Reader) {
			closeReader((Reader) obj);
		} else if (obj instanceof RandomAccessFile) {
			closeFile((RandomAccessFile) obj);
		} else if (obj instanceof Socket) {
			closeSocket((Socket) obj);
		} else if (obj instanceof ServerSocket) {
			closeServerSocket((ServerSocket) obj);
		} else if (obj instanceof Process) {
			closeProcess((Process) obj);
		} else if (obj instanceof Cursor) {
			closeCursor((Cursor) obj);
		} else if (obj instanceof Closeable) {
			close((Closeable) obj);
		} else {
			throw new RuntimeException("unSupport");
		}
	}
	
}
