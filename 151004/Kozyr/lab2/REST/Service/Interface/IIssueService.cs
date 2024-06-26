﻿using REST.Entity.Db;
using REST.Entity.DTO.RequestTO;
using REST.Entity.DTO.ResponseTO;
using REST.Service.Interface.Common;

namespace REST.Service.Interface
{
    public interface IIssueService : ICrudService<Issue, IssueRequestTO, IssueResponseTO>
    {
    }
}
