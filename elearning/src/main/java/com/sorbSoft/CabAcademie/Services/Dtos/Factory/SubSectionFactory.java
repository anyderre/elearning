package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubSectionViewModel;

public class SubSectionFactory {

    public static SubSectionViewModel getSubSectionViewModel(){
        return new SubSectionViewModel() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public Section getSection() {
                return new Section() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }
        };
    }
}
