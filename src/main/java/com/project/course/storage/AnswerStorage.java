package com.project.course.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.course.entity.AnswerEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerStorage {

    private Map<String, String> map = new HashMap<>();

    public void load() throws IOException {

        File file = new File("answers.json");
        if (!file.isFile()){
            return;
        }

        FileInputStream in = new FileInputStream(file.getPath());
        byte[] buffer = new byte[in.available()];
        in.read(buffer, 0, buffer.length);

        String output = new String(buffer, StandardCharsets.UTF_8);
        List<AnswerEntity> list = new Gson().fromJson(output, new TypeToken<List<AnswerEntity>>() {}.getType());
        for (AnswerEntity answerEntity : list){
            map.put(answerEntity.getQuestion(), answerEntity.getAnswer());
        }
    }

    public String getAnswer(String question){
        String answer = map.get(question);
        return answer == null ? "Я не понимаю тебя." : answer;
    }

}
