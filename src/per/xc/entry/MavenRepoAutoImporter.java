package per.xc.entry;

import java.io.IOException;

/**
 * maven 仓库自动导入到本地，可用于迁移maven仓库
 * 注意，maven库必须在根目录中，否侧程序会执行出错
 * This Class can help you install local jar to local repository automatically
 * when you want to move local repository to another place.
 * you just input local repository path and
 * local repository should be in root directory. ----> That is very important!!
 * @version V1.0
 * @Author XiongCheng
 * @Date 2018/4/17 12:29.
 */
public class MavenRepoAutoImporter {
    public static void main(String[] args) throws IOException {
        //本地maven库的路径
        //You just need to replace with your local repository path.
        String repositoryPath = "D:\\MavenRepo";
        Importer.findJars(repositoryPath);
        //生成的批处理文件的位置
        //Indicating where the bat file will be save in.
        String saveFilePath = "D:\\action.bat";
        Importer.writeFile(saveFilePath);
    }
}
