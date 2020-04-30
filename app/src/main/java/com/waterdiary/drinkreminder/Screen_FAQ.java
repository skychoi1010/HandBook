package com.waterdiary.drinkreminder;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.custom.AnimationUtils;
import com.waterdiary.drinkreminder.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

import static com.waterdiary.drinkreminder.R.id;

public class Screen_FAQ extends MasterBaseActivity
{
    LinearLayout right_icon_block,left_icon_block;
    AppCompatTextView lbl_toolbar_title;

    List<FAQModel> lst_faq=new ArrayList<>();
    LinearLayout faq_block;

    List<LinearLayout> answer_block_lst=new ArrayList<>();
    List<ImageView> img_faq_lst=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_faq);

        FindViewById();
        Body();
    }

    private void FindViewById()
    {
        right_icon_block=findViewById(id.right_icon_block);
        left_icon_block=findViewById(id.left_icon_block);
        lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);

        faq_block=findViewById(R.id.faq_block);
    }

    private void Body()
    {
        lbl_toolbar_title.setText(sh.get_string(R.string.str_faqs));
        left_icon_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_icon_block.setVisibility(View.GONE);

        setFAQData();
        loadFAQData();
    }

    public void setFAQData()
    {
        FAQModel faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_1));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_1));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_2));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_2));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_3));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_3));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_12));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_12));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_13));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_13));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_4));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_4));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_11));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_11));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_5));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_5));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_6));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_6));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_7));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_7));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_8));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_8));
        lst_faq.add(faqModel);

        faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_9));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_9));
        lst_faq.add(faqModel);

        /*faqModel=new FAQModel();
        faqModel.setQuestion(sh.get_string(R.string.faq_question_10));
        faqModel.setAnswer(sh.get_string(R.string.faq_answer_10));
        lst_faq.add(faqModel);*/
    }

    public void loadFAQData()
    {


        faq_block.removeAllViews();
        for(int k=0;k<lst_faq.size();k++)
        {
            final int pos=k;

            final FAQModel rowModel = lst_faq.get(k);

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View itemView = layoutInflater.inflate(R.layout.row_item_faq, null, false);

            AppCompatTextView lbl_question=itemView.findViewById(R.id.lbl_question);
            AppCompatTextView lbl_answer=itemView.findViewById(R.id.lbl_answer);

            LinearLayout question_block=itemView.findViewById(R.id.question_block);
            final LinearLayout answer_block=itemView.findViewById(R.id.answer_block);
            final ImageView img_faq=itemView.findViewById(R.id.img_faq);

            answer_block_lst.add(answer_block);
            img_faq_lst.add(img_faq);

            lbl_question.setText(rowModel.getQuestion());
            lbl_answer.setText(rowModel.getAnswer());

            question_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answer_block.getVisibility()==View.GONE)
                    {
                        viewAnswer(pos);
                        img_faq.setImageResource(R.drawable.ic_faq_minus);
                        AnimationUtils.expand(answer_block);
                    }
                    else
                    {

                        img_faq.setImageResource(R.drawable.ic_faq_plus);
                        AnimationUtils.collapse(answer_block);
                    }
                }
            });

            faq_block.addView(itemView);
        }
    }

    public void viewAnswer(int pos)
    {
        for(int k=0;k<answer_block_lst.size();k++)
        {
            if(k==pos)
                continue;
            else
            {
                img_faq_lst.get(k).setImageResource(R.drawable.ic_faq_plus);
                AnimationUtils.collapse(answer_block_lst.get(k));
            }
        }
    }
}