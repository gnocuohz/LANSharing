package team.wwg.lansharing.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	private static String savePath = "E:/cliant/";
	
	/**
	 * 
	 * @param Fileuri  文件的uri 如 D:/dsd/dsd.txt
	 * @return  文件 名                     如 dsd.txt
	 */
	public static String getFileAllName(String Fileuri){
		
		int i = Fileuri.lastIndexOf("\\");
		String fileName = Fileuri.substring(i + 1);
		return fileName;
		
	}
	
	
	public static String getRightUri(String Fileuri){
		
		String fileName = Fileuri.replaceAll("\\\\", "/");
	
		return fileName;
	}
	
	
	
	public static void setDefaultPath(String str) {
		
		savePath = getRightUri(str);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 获取完整的路径，返回 新的文件
	 * @param fileUrl  对方的文件路径  如  D:/txds.txt
	 * @return 返回本地将要保存的文件。 若重名则在末尾+_copy 副本标识 如  "E:/cliant/txds.txt 
	 * 												 的副本： E:/cliant/txds_copy.txt
	 */
	
	public static File getTargetFile(String fileUrl){
		
		String	fileName = FileUtil.getFileAllName(fileUrl);
		
		
		
		File filepath =  new File(savePath);
		
		if(!filepath.exists() && !filepath .isDirectory()){
			filepath.mkdir();
		}
		
		File file =  new File(savePath+fileName);
		file =getOnlyfile(fileName,file);
		
		return file;
	}
	

	

	
	/**
	 *  获取完整的路径，并填入要另存为的文件夹路径   返回 新的文件
	 * @param fileUrl  对方的文件路径  如  D:/txds.txt
	 * @param newPath  自己要另存为的地址
	 * @return  返回最终保存的文件
	 * 			返回本地将要保存的文件。 若重名则在末尾+_copy 副本标识 如  "E:/cliant/txds.txt 
	 * 												 的副本： E:/cliant/txds_copy.txt
	 */
	public static File getTargetFile(String fileUrl , String newPath){
		
		String	fileName = FileUtil.getFileAllName(fileUrl);
		
		String newpath_ =  getRightUri(newPath)+"/";
		
		File filepath =  new File(newpath_);
		
		if(!filepath.exists() && !filepath .isDirectory()){
			filepath.mkdir();
		}
		
		File file =  new File(newpath_+fileName);
		file =getOnlyfile(fileName,file,newpath_);
		
		return file;
	}
	
	
	
	
	/**
	 *  获取文件的类型 后缀名  如 dsad。txt  
	 * @param fileuri  文件名或者文件路径
	 * @return  返回文件类型  如    txt
	 */
	public static String getFiletype(String  fileuri){
		int i = fileuri.lastIndexOf(".");
		String filetype = fileuri.substring(i + 1);
		return filetype;
	}
	

	/**
	 * 获取文件的名字    
	 * @param fileUri 文件uri  如D:/ds颠三倒四d/dsd.txt
	 * @return     dsd
	 */
	public static String getFileName(String fileUri){
		
		String fileAllname =getFileAllName(fileUri);
		
		int i = fileAllname.lastIndexOf(".");
		
		String filename = fileAllname.substring(0,i);
	
		return filename;
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 判断文件名字是否存在，如果存在，则添加后缀
	 * @param filename 要判断的文件名
	 * @return
	 */
	public static File getOnlyfile(String fileName ,File file){
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{
			
			fileName = getFileName(fileName)+"_copy"+"."+getFiletype(fileName);
			
			file = new File(savePath+fileName);
			
			file = getOnlyfile(fileName,file);
		}
		
		return file;
		
	}
	
	
	
	/**
	 * 判断文件名字是否存在，如果存在，则添加后缀
	 * @param filename 要判断的文件名
	 * @return
	 */
	public static File getOnlyfile(String fileName ,File file,String newpath){
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{
			
			fileName = getFileName(fileName)+"_copy"+"."+getFiletype(fileName);
			
			file = new File(newpath+fileName);
			
			file = getOnlyfile(fileName,file,newpath);
		}
		
		return file;
		
	}
	
	
	
	
	public static String showFilelength(long  thelenth){
		
		
		String str =null;
		if(thelenth>=1024){
			thelenth = thelenth/1024;
			if(thelenth>1024){
				thelenth = thelenth/1024;
				
				if(thelenth>1024){
					thelenth = thelenth/1024;
					if(thelenth>1024){
						thelenth = thelenth/1024;
						
					}else{
						str = thelenth+"GB";
					}
					
				}else{
					str = thelenth+"MB";
				}
			}else{
				str = thelenth+"KB";
			}
				
		}else{
			str = thelenth+"B";
		}
		
		return str;
		
	}
	
	
	
	
}
