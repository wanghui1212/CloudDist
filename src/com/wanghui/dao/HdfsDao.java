package com.wanghui.dao;

import java.io.IOException;
import java.io.InputStream;
//import java.util.Scanner;
//import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsDao {
	static Configuration conf = new Configuration();
	static String hdfsPath = "hdfs://192.168.137.128:9000/wanghui-3120002988/user";
	static String fileRootPath = "/wanghui-3120002988/user/";

	public static void init() {
		try {
			conf.set("fs.defaultFS", hdfsPath);
			conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		} catch (Exception e) {
			System.out.println("你似乎没有运行hadoop哦！！");
			e.printStackTrace();
		}
	}

//  根据用户和文件名判断文件是否存在
	public static boolean ifFileExist(String user, String filename) throws IOException {
		init();
		String fileName = fileRootPath + user + "/" + filename;// 你的文件路径，没有就显示不存在
		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(new Path(fileName))) {
			System.out.println("文件存在");
			return true;
		} else {
			System.out.println("文件不存在");
			return false;
		}
	}

// 为每个用户创建一个目录
	public static void mkPersonalDir(String username) throws IOException {
		init();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		String upremote = fileRootPath + username;
		Path a = new Path(upremote);
		fs.mkdirs(a);
		System.out.println("创建个人目录成功");
		fs.close();
	}
	// 创建一个子目录
	public static void mkChildDir(String filePath) throws IOException {
		init();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		String upremote = fileRootPath + filePath;
		Path a = new Path(upremote);
		fs.mkdirs(a);
		System.out.println("创建目录成功");
		fs.close();
	}
	// 根据路径删除文件
	public static void deleteFile(String deletePath) throws IOException {
		init();
//		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		String filePath = fileRootPath + deletePath;
		fs.deleteOnExit(new Path(filePath));
		fs.close();
	}
	// 遍历用户目录文件与目录
	public static FileStatus[] showFiles(String username) throws IOException {
		init();
		String filePath = fileRootPath + username;
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FileStatus[] list = fs.listStatus(new Path(filePath));
		fs.close();
		return list;
	}
	// 遍历用户目录下的子文件夹
	public static FileStatus[] showDirFiles(String filePath) throws IOException {
		init();
		String filePath1 = fileRootPath + filePath + "/";
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FileStatus[] list = fs.listStatus(new Path(filePath1));
		if (list != null) {
			for (FileStatus f : list) {
				System.out.printf("name %s,folder:%s,size:%d\n", f.getPath(), f.isDir(), f.getLen());
			}
		}
		fs.close();
		return list;
	}
//	获取文件的输入流对象
	public static InputStream downloadFile(String cloudPath) throws IOException, InterruptedException, URISyntaxException {
		// 1获取对象
		init();
		String filePath1 = fileRootPath + cloudPath + "/";
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FSDataInputStream in = fs.open(new Path(filePath1));
		return in;
	}
	public static void uploadFile(String fileName,InputStream in) throws IllegalArgumentException, IOException, InterruptedException, URISyntaxException{
		init();
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FSDataOutputStream out = fs.create(new Path(fileRootPath + fileName + "/"));
//		FileInputStream in = new FileInputStream("D:/新建.txt");
		IOUtils.copy(in, out); 
		fs.close();
		
	}
//	移动或者重命名:path1原文件路径，path2粘贴路径
	public static void reNameFile(String path1,String path2) throws IOException {
		// 1获取对象
		init();
		String filePath1 = fileRootPath + path1 + "/";
		String filePath2 = fileRootPath + path2 + "/";

		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		boolean re = fs.rename(new Path(filePath1), new Path(filePath2));
        System.out.println("rename:"+re);
	}
}
