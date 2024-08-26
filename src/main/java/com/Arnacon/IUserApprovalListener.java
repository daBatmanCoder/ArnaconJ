package com.Arnacon;

public interface IUserApprovalListener {
    void requestUserApproval(String action, IApprovalCallback callback);
}
