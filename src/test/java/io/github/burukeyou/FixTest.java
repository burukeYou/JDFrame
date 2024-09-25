package io.github.burukeyou;

import io.github.burukeyou.data.Student;
import io.github.burukeyou.data.UserInfo;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FixTest extends JDFrameTest {

    @Test
    public void testJoinEmpty(){
        //List<UserInfo> userInfos = Arrays.asList(new UserInfo("a", 99), new UserInfo("a", 4), new UserInfo("b", 4));
        List<UserInfo> userInfos = new ArrayList<>();
        SDFrame<UserInfo> userFrame = SDFrame.read(userInfos);
        SDFrame.read(studentList)
               .leftJoinVoid(userFrame, JoinOn.on(Student::getName,UserInfo::getKey1),(stu,user) -> {
                    System.out.println(stu + "====>" + user);
               });
        System.out.println("------------------------------------> ");
        SDFrame<String> frame = SDFrame.read(studentList)
                .leftJoin(userFrame, JoinOn.on(Student::getName, UserInfo::getKey1), (stu, user) -> {
                    return (user == null) ? stu.getId()+"" : (stu.getId() + user.getKey1());
                });
        frame.show();
    }
}
