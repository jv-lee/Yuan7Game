package com.y7.smspay.mp.srv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

public class JM {
    private static JM instance = null;
    private String jarName = new String(Base64.decode("anBkYXRhLmRhdA==", 0)).trim();
    private final String jz_jar_name = new String(Base64.decode("Y29tLmd1YWl3dS5zZGsubWdyLllQb3lNYW5hZ2Vy", 0)).trim();
    //private final String jz_jar_name = new String("com.guaiwu.sdk.mgr.YPoyManager").trim();
    private IMstP payBiz = null;
    private final String unit_jar_path = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/.aoy/units/");
//    public int ver = 903;
    private static Context context = null;
    
	public static String MB_UA = null;
    
    public static synchronized JM i(Context cxt) {
        JM jm;
        synchronized (JM.class) {
           // if (context == null) {
            	context = cxt;
           // }
            if (instance == null) {
                instance = new JM();
                instance.init();
            }
            jm = instance;
        }
        return jm;
    }

    private void init() {
        if (!loadJar()) {
            
        }
        loadJar();
    }

    public IMstP getPay() {
        return this.payBiz;
    }

    @SuppressLint("NewApi")
	private IMstP loadPayBiz(File dex) {
        try {
            return (IMstP) new DexClassLoader(dex.toString(), JM.context.getDir("dex", 0).getAbsolutePath(), null, getClass().getClassLoader()).loadClass(this.jz_jar_name).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (InstantiationException e4) {
            e4.printStackTrace();
        } catch (IllegalAccessException e5) {
            e5.printStackTrace();
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
        } catch (Exception e7) {
            e7.printStackTrace();
        }
        return null;
    }

    public void putJar(InputStream in, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(this.unit_jar_path + filename + ".tmp");
        byte[] buff = new byte[1024];
        while (true) {
            int readed = in.read(buff);
            if (readed > 0) {
                fos.write(buff, 0, readed);
            } else {
                fos.close();
                new File(this.unit_jar_path + filename + ".tmp").renameTo(new File(this.unit_jar_path + filename));
                return;
            }
        }
    }

    public boolean jarExists(String filename) {
        return new File(this.unit_jar_path + filename).exists();
    }

    private boolean loadJar() {
        File jarFile = getJarFileDir();
		//DDDLog.d("loadJar");
        if (!jarFile.exists()) {
            return false;
        }
        IMstP loadPayBiz = loadPayBiz(jarFile);
        if (loadPayBiz == null) {
            return false;
        }
        this.payBiz = loadPayBiz;
        return true;
    }

    public File getJarFileDir() {
       
    	File jarFile = null;
        FileOutputStream fos=null;
        ByteArrayOutputStream byteArrayOutputStream=null;
        BufferedInputStream bufferedInputStream=null;
		try {
			InputStream fis = JM.context.getResources().getAssets().open("patch.jar");
			jarFile = new File(JM.context.getFilesDir()+"/"+"pay.jar");
            byteArrayOutputStream=new ByteArrayOutputStream();
            bufferedInputStream=new BufferedInputStream(fis);
            byte[] b=new byte[1024];
            int len=0;
            while ((len=bufferedInputStream.read(b))!=-1){
                byteArrayOutputStream.write(b,0,len);
                byteArrayOutputStream.flush();
            }

            fos = new FileOutputStream(jarFile);
            fos.write(byteArrayOutputStream.toByteArray());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {

                try {
                    if (fos!=null)
                    fos.close();
                    if (byteArrayOutputStream!=null)
                        byteArrayOutputStream.close();
                    if (bufferedInputStream!=null)
                        bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return jarFile;
    }

    private void copyAssetsJoloJar() throws IOException {
//        File jarFile = getJarFileDir();
//   		if (jarFile.exists()){
//   			jarFile.delete();
//   		}
//   		try {
//   			FileOutputStream out = new FileOutputStream(jarFile);
//   			out.write(ac.readDatFile(JM.context));
//   			out.flush();
//   			out.close();
//   		} catch (Exception e) {
//   			e.printStackTrace();
//   		}
      /*  InputStream fis = JM.context.getResources().getAssets().open(this.jarName);
        File jarFile = getJarFileDir();
        File tmpFile = new File(jarFile.getPath() + ".tmp");
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        FileOutputStream fos = new FileOutputStream(tmpFile);
        byte[] buff = new byte[1024];
        while (true) {
            int readed = fis.read(buff);
            if (readed > 0) {
                fos.write(buff, 0, readed);
            } else {
                fis.close();
                fos.close();
                tmpFile.renameTo(jarFile);
                return;
            }
        }*/
    }

    public void reloadJar() {
    	//DDDLog.d("reload jar");
        if (!loadJar()) {
            try {
                copyAssetsJoloJar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadJar();
    }
}
